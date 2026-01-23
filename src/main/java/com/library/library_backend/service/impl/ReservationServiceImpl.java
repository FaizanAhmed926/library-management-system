package com.library.library_backend.service.impl;

import com.library.library_backend.domain.BookLoanStatus;
import com.library.library_backend.domain.ReservationStatus;
import com.library.library_backend.domain.UserRole;
import com.library.library_backend.mapper.ReservationMapper;
import com.library.library_backend.model.Book;
import com.library.library_backend.model.Reservation;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.ReservationDTO;
import com.library.library_backend.payload.request.CheckoutRequest;
import com.library.library_backend.payload.request.ReservationRequest;
import com.library.library_backend.payload.request.ReservationSearchRequest;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.repository.BookLoanRepository;
import com.library.library_backend.repository.BookRepository;
import com.library.library_backend.repository.ReservationRepository;
import com.library.library_backend.service.BookLoanService;
import com.library.library_backend.service.ReservationService;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private  final ReservationRepository reservationRepository;
    private  final ReservationMapper reservationMapper;
    private  final BookLoanService bookLoanService;

    int MAX_RESERVATION=5;

    @Override
    public ReservationDTO createReservation(ReservationRequest reservationRequest) throws Exception {
        User user=userService.getCurrentUser();
        return createReservationForUser(reservationRequest,user.getId());
    }

    @Override
    public ReservationDTO createReservationForUser(ReservationRequest reservationRequest, Long userId) throws Exception {
        boolean alreadyHasLoan=bookLoanRepository.existsByUserIdAndBookIdAndStatus(
                userId, reservationRequest.getBookId() , BookLoanStatus.CHECKED_OUT
        );
        if (alreadyHasLoan){
            throw new Exception("You already have loan on this book");
        }
        User user=userService.getCurrentUser();

        Book book=bookRepository.findById(reservationRequest.getBookId())
                .orElseThrow(()->new  Exception("Book not Found"));
        if (reservationRepository.hasActiveReservation(userId,book.getId())){
            throw  new Exception("You already have active reservation on this book");
        }
        if (book.getAvailableCopies()>0){
            throw new Exception("Book is already available");
        }
        long activeReservations=reservationRepository.countActiveReservationsByUser(userId);
        if (activeReservations>=MAX_RESERVATION){
            throw new Exception("You have reserved "+ MAX_RESERVATION +"times");
        }
        Reservation reservation=new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSend(false);
        reservation.setNotes(reservationRequest.getNotes());

        long pendingCount=reservationRepository.countPendingReservationsByBook(book.getId());
        reservation.setQueuePosition((int) pendingCount+1);

        Reservation savedReservation=reservationRepository.save(reservation);
        System.out.println(savedReservation.getBook().getTitle()+ "nnnnnnnn");
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) throws Exception {
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new Exception("Reservation not found with ID: "+reservationId));

        User currentUser=userService.getCurrentUser();
        if (!reservation.getUser().getId().equals(currentUser.getId()) && currentUser.getRole()!= UserRole.ROLE_ADMIN){
            throw new Exception("You can only cancel your own reservations");
        }
        if (!reservation.canBeCancelled()){
            throw  new Exception("Reservation cannot be cancelled ( current status: "+ reservation.getStatus() +")");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());

        Reservation savedReservation=reservationRepository.save(reservation);


        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws Exception {
        Reservation reservation=reservationRepository.findById(reservationId)
                .orElseThrow(()->new Exception("Reservation not found with ID : "+reservationId));
        if (reservation.getBook().getAvailableCopies()<=0){
            throw new Exception("Reservation  is not available for pickup (current status :"+reservation.getStatus()+")");
        }
        reservation.setStatus(ReservationStatus.FULLFILLED);
        reservation.setFullfilledAt(LocalDateTime.now());

        Reservation savedReservation=reservationRepository.save(reservation);
        CheckoutRequest request=new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        request.setNotes("Assign Booked by Admin");
        bookLoanService.checkoutBookForUser(reservation.getUser().getId(),request);
        return reservationMapper.toDTO(savedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest) throws Exception {
        User user=userService.getCurrentUser();
        searchRequest.setUserId(user.getId());
        return searchReservations(searchRequest);
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest) {
        Pageable pageable=createPageable(searchRequest);
        Page<Reservation> reservationPage=reservationRepository.searchReservationsWithFilters(
                searchRequest.getUserId(),
                searchRequest.getBookId(),
                searchRequest.getStatus(),
                searchRequest.getActiveOnly()!=null ? searchRequest.getActiveOnly() : false,
                pageable
        );
        return builtPageResponse(reservationPage);
    }

    private  PageResponse<ReservationDTO> builtPageResponse(Page<Reservation> reservationPage){
        List<ReservationDTO> dtos=reservationPage.getContent().stream()
                .map(reservationMapper::toDTO)
                .toList();
        PageResponse<ReservationDTO> response=new PageResponse<>();
        response.setContent(dtos);
        response.setPageNumber(reservationPage.getNumber());
        response.setPageSize(reservationPage.getSize());
        response.setTotalElement(reservationPage.getTotalElements());
        response.setTotalPages(reservationPage.getTotalPages());
        response.setLast(reservationPage.isLast());

        return response;
    }

    private Pageable createPageable(ReservationSearchRequest searchRequest){
        Sort sort="ASC".equalsIgnoreCase(searchRequest.getSortDirection())
                ? Sort.by(searchRequest.getSortBy()).ascending()
                : Sort.by(searchRequest.getSortBy()).descending();
        return PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
    }
}
