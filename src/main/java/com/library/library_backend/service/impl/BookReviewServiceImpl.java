package com.library.library_backend.service.impl;

import com.library.library_backend.domain.BookLoanStatus;
import com.library.library_backend.mapper.BookReviewMapper;
import com.library.library_backend.model.Book;
import com.library.library_backend.model.BookLoan;
import com.library.library_backend.model.BookReview;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.BookReviewDTO;
import com.library.library_backend.payload.request.CreateReviewRequest;
import com.library.library_backend.payload.request.UpdateReviewRequest;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.repository.BookLoanRepository;
import com.library.library_backend.repository.BookRepository;
import com.library.library_backend.repository.BookReviewRepository;
import com.library.library_backend.service.BookReviewService;
import com.library.library_backend.service.BookService;
import com.library.library_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookService bookService;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final BookReviewMapper bookReviewMapper;
    private final BookLoanRepository bookLoanRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest request) throws Exception {
        User user=userService.getCurrentUser();

        Book book=bookRepository.findById(request.getBookId())
                .orElseThrow(()->new  Exception("Book not Found"));

        if (bookReviewRepository.existsByUserIdAndBookId(user.getId(),book.getId())){
            throw new Exception("You have already reviewed this book");
        }
        boolean  hasReadBook=hasUserReadBook(user.getId(),book.getId());
        if (!hasReadBook){
            throw new Exception("You have not read this book!");
        }
        BookReview bookReview=new BookReview();
        bookReview.setUser(user);
        bookReview.setBook(book);
        bookReview.setRating(request.getRating());
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());
        BookReview savedBookReview=bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception {
        User user=userService.getCurrentUser();
        BookReview bookReview=bookReviewRepository.findById(reviewId)
                .orElseThrow(()->new  Exception("Review not found"));
        if (!bookReview.getUser().getId().equals(user.getId())){
            throw new Exception("You have not reviewed this book!");
        }
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());
        bookReview.setRating(request.getRating());
        BookReview savedBookReview=bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws Exception {
        User user=userService.getCurrentUser();
        BookReview bookReview=bookReviewRepository.findById(reviewId)
                .orElseThrow(()->new  Exception("Review not found with id: "+ reviewId));
        if (!bookReview.getUser().getId().equals(user.getId())){
            throw new Exception("You can only delete your own reviews");
        }
        bookReviewRepository.delete(bookReview);
    }

    @Override
    public PageResponse<BookReviewDTO> getReviewsByBookId(Long id, int page, int size) throws Exception {
        Book book=bookRepository.findById(id)
                .orElseThrow(()->new  Exception("Book not Found by id!"));
        Pageable pageable= PageRequest.of(page,size,
                Sort.by("CreatedAt").descending());
        Page<BookReview> reviewPage=bookReviewRepository.findByBook(book,pageable);
        return convertToPageResponse(reviewPage);
    }

    private PageResponse<BookReviewDTO> convertToPageResponse(Page<BookReview> reviewPage) {
        List<BookReviewDTO> reviewDTOS=reviewPage.getContent()
                .stream()
                .map(bookReviewMapper::toDTO)
                .collect(Collectors.toList());
        return  new PageResponse<>(
                reviewDTOS,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isLast(),
                reviewPage.isFirst(),
                reviewPage.isEmpty()
        );
    }

    private boolean hasUserReadBook(Long userId,Long bookId){
        List<BookLoan> bookLoans=bookLoanRepository.findByBookId(bookId);
        return bookLoans.stream().anyMatch(loan->loan.getUser().getId().equals(userId) &&
                loan.getStatus()== BookLoanStatus.RETURNED);
    }

}
