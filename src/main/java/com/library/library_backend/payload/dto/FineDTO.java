package com.library.library_backend.payload.dto;

import com.library.library_backend.domain.FineStatus;
import com.library.library_backend.domain.FineType;
import com.library.library_backend.model.BookLoan;
import com.library.library_backend.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FineDTO {
    private Long id;

    @NotNull(message = "UserId is mandatory")
    private Long userId;
    private String userName;
    private String userEmail;

    @NotNull(message = "Book Loan Id is mandatory")
    private Long bookLoanId;
    private String bookTitle;
    private String bookIsbn;

    @NotNull(message = "Fine Type is mandatory")
    private FineType type;

    @PositiveOrZero(message = "Fine amount cannot be zero")
    private  Long amount;
    private  Long amountPaid;
    private  Long amountOutstanding;

    @NotNull(message = "Fine Status is mandatory")
    private FineStatus status;

    private String reason;

    private String notes;

    private Long waivedByUserId;
    private String waivedByUserName;


    private LocalDateTime waivedAt;

    private String waiverReason;

    private  LocalDateTime paidAt;

    private Long processedByUserId;
    private String processByUserName;

    private String transactionId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
