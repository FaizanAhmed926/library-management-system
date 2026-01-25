package com.library.library_backend.controller;

import com.library.library_backend.payload.dto.BookReviewDTO;
import com.library.library_backend.payload.request.CreateReviewRequest;
import com.library.library_backend.payload.request.UpdateReviewRequest;
import com.library.library_backend.payload.response.ApiResponse;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.service.BookReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class BookReviewController {
    private final BookReviewService bookReviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody CreateReviewRequest request) throws Exception {
        BookReviewDTO reviewDTO=bookReviewService.createReview(request);
        return ResponseEntity.ok(reviewDTO);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> updateReview(@PathVariable Long id, @Valid @RequestBody UpdateReviewRequest request) throws Exception {
        BookReviewDTO reviewDTO=bookReviewService.updateReview(id,request);
        return ResponseEntity.ok(reviewDTO);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) throws Exception {
        bookReviewService.deleteReview(reviewId);
        return  ResponseEntity.ok(new ApiResponse("Review deleted successfully",true));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<PageResponse<BookReviewDTO>> getReviewsByBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws Exception {
        PageResponse<BookReviewDTO> reviews=bookReviewService.getReviewsByBookId(bookId,page,size);
        return ResponseEntity.ok(reviews);
    }
}
