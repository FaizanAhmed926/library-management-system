package com.library.library_backend.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.library_backend.model.Book;
import com.library.library_backend.model.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewDTO {

    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;
    private String userName;

    @NotNull(message = "Book ID is mandatory")
    private Long bookId;
    private String bookTitle;

    @NotNull(message = "Rating is mandatory")
    @Min(value = 1,message = "Rating must be at least 1")
    @Max(value = 5,message = "Rating must not exceed 5")
    private Integer rating;

    @NotNull(message = "Review text is mandatory")
    @Size(min = 10,max = 2000,message = "Review must be between 10 and 2000 characters")
    private String reviewText;

    @Size(max = 200,message = "Review title must not exceed 200 characters")
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private  LocalDateTime updatedAt;
}
