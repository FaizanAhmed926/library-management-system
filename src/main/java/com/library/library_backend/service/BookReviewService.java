package com.library.library_backend.service;

import com.library.library_backend.payload.dto.BookReviewDTO;
import com.library.library_backend.payload.request.CreateReviewRequest;
import com.library.library_backend.payload.request.UpdateReviewRequest;
import com.library.library_backend.payload.response.PageResponse;

public interface BookReviewService {
    BookReviewDTO createReview(CreateReviewRequest request) throws Exception;

    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) throws Exception;

    void deleteReview(Long reviewId) throws Exception;

    PageResponse<BookReviewDTO> getReviewsByBookId(Long id,int page, int size) throws Exception;
}
