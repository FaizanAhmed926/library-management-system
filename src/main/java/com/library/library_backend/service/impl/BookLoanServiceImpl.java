package com.library.library_backend.service.impl;

import com.library.library_backend.domain.BookLoanStatus;
import com.library.library_backend.domain.BookLoanType;
import com.library.library_backend.exception.BookException;
import com.library.library_backend.mapper.BookLoanMapper;
import com.library.library_backend.model.Book;
import com.library.library_backend.model.BookLoan;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.BookLoanDTO;
import com.library.library_backend.payload.dto.SubscriptionDTO;
import com.library.library_backend.payload.request.BookLoanSearchRequest;
import com.library.library_backend.payload.request.CheckinRequest;
import com.library.library_backend.payload.request.CheckoutRequest;
import com.library.library_backend.payload.request.RenewalRequest;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.repository.BookLoanRepository;
import com.library.library_backend.repository.BookRepository;
import com.library.library_backend.service.BookLoanService;
import com.library.library_backend.service.SubscriptionService;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private  final UserService userService;
    private  final SubscriptionService subscriptionService;
    private final BookRepository bookRepository;
    private final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanDTO checkoutBook(CheckoutRequest checkoutrequest) throws Exception {
        User user=userService.getCurrentUser();
        return checkoutBookForUser(user.getId(),checkoutrequest);
    }

    @Override
    public BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutrequest) throws Exception {
        User user=userService.findById(userId);
        SubscriptionDTO subscription=subscriptionService.getUsersActiveSubscription(user.getId());
        Book book=bookRepository.findById(checkoutrequest.getBookId()).orElseThrow(()->new BookException("Book not found with id "+checkoutrequest.getBookId()));
        if (!book.getActive()){
            throw new BookException("Book is not available");
        }
        if (book.getAvailableCopies()<=0){
            throw new BookException("Book is not available");
        }
        if (bookLoanRepository.hasActiveCheckout(userId,book.getId())){
            throw new BookException("Book already has active checkout");
        }
        long activeCheckouts=bookLoanRepository.countActiveBookLoansByUser(userId);
        int maxBooksAllowed= subscription.getMaxBooksAllowed();

        if (activeCheckouts>=maxBooksAllowed){
            throw new Exception("You have reached your maximum number of books allowed");
        }

        long overdueCount=bookLoanRepository.countOverdueBookLoansByUser(userId);
        if(overdueCount>0){
            throw new Exception("First return old overdue book!");
        }

        BookLoan bookLoan=BookLoan
                .builder()
                .user(user)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKED_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(checkoutrequest.getCheckoutDays()))
                .renewalCount(0)
                .maxRenewals(2)
                .notes(checkoutrequest.getNotes())
                .isOverdue(false)
                .overdueDays(0)
                .build();
        book.setAvailableCopies(book.getAvailableCopies()-1);
        bookRepository.save(book);
        BookLoan savedBookLoan=bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO checkinBook(CheckinRequest checkinrequest) throws Exception {
        BookLoan bookLoan=bookLoanRepository.findById(checkinrequest.getBookLoanId())
                .orElseThrow(()->new Exception("Book Loan  not found"));

        if(!bookLoan.isActive()){
            throw  new BookException("Book loan is not Active");
        }

        bookLoan.setReturnDate(LocalDate.now());
        BookLoanStatus condition=checkinrequest.getCondition();
        if (condition==null){
            condition=BookLoanStatus.RETURNED;
        }
        bookLoan.setStatus(condition);
        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);

        bookLoan.setNotes("Book returned by user");

        if (condition!=BookLoanStatus.LOST){
            Book book=bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies()+1);
            bookRepository.save(book);
        }
        BookLoan savedBookLoan=bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO renewcheckout(RenewalRequest renewalRequest) throws Exception {
        BookLoan bookLoan=bookLoanRepository.findById(renewalRequest.getBookLoanId())
                .orElseThrow(()->new Exception("Book Loan  not found"));
        if (!bookLoan.canRenew()){
            throw new BookException("Book cannot be renewed");
        }
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(renewalRequest.getExtensionDay()));
        bookLoan.setRenewalCount(bookLoan.getRenewalCount()+1);
        bookLoan.setNotes("Book renewed by user");
        BookLoan savedBookLoan=bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception {
        User currentUser=userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;
        if (status!=null){
            Pageable pageable= PageRequest.of(page,size, Sort.by("dueDate").ascending());
            bookLoanPage=bookLoanRepository.findByStatusAndUser(status,currentUser,pageable);
        }
        else {
            Pageable pageable=PageRequest.of(page,size,Sort.by("createdAt").descending());
            bookLoanPage=bookLoanRepository.findByUserId(currentUser.getId(),pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest searchRequest) {
        Pageable pageable=createPageable(
                searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection()
        );
        Page<BookLoan> bookLoanPage;
        if (Boolean.TRUE.equals(searchRequest.getOverdueOnly())){
            bookLoanPage=bookLoanRepository.findOverdueBookLoans(LocalDate.now(),pageable);
        } else if (searchRequest.getUserId()!=null) {
            bookLoanPage=bookLoanRepository.findByUserId(searchRequest.getUserId(),pageable);
        } else if (searchRequest.getBookId()!=null) {
            bookLoanPage=bookLoanRepository.findByBookId(searchRequest.getBookId(),pageable);
        } else if (searchRequest.getStatus()!=null) {
            bookLoanPage=bookLoanRepository.findByStatus(searchRequest.getStatus(),pageable);
        } else if (searchRequest.getStartDate()!=null && searchRequest.getEndDate()!=null) {
            bookLoanPage=bookLoanRepository.findBookLoansByDateRange(
                    searchRequest.getStartDate(),
                    searchRequest.getEndDate(),
                    pageable
            );
        } else {
            bookLoanPage =bookLoanRepository.findAll(pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public int updateOverdueBookLoan() {
        Pageable pageable=PageRequest.of(0,1000);
        Page<BookLoan> overduePage=bookLoanRepository.findOverdueBookLoans(LocalDate.now(),pageable);
        int updateCount=0;
        for (BookLoan bookLoan:overduePage.getContent()){
            if (bookLoan.getStatus()==BookLoanStatus.CHECKED_OUT){
                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverdue(true);
                int overdueDays=calculateOverdueDate(bookLoan.getDueDate(),LocalDate.now());
                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }
        return 0;
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection){
        size=Math.min(size,100);
        size=Math.max(size,1);
        Sort sort=sortDirection.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return  PageRequest.of(page,size,sort);
    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoan> bookLoanPage){
        List<BookLoanDTO> bookLoanDTOs=bookLoanPage.getContent().stream().map(bookLoanMapper::toDTO).collect(Collectors.toList());
        return new PageResponse<>(
                bookLoanDTOs,
                bookLoanPage.getNumber(),
                bookLoanPage.getSize(),
                bookLoanPage.getTotalElements(),
                bookLoanPage.getTotalPages(),
                bookLoanPage.isLast(),
                bookLoanPage.isFirst(),
                bookLoanPage.isEmpty()
        );
    }

    public  int calculateOverdueDate(LocalDate dueDate,LocalDate today){
        if (today.isBefore(dueDate) || today.isEqual(dueDate)){
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate,today);
    }
}
