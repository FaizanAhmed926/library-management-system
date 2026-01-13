package com.library.library_backend.payload.dto;

import com.library.library_backend.model.SubscriptionPlan;
import com.library.library_backend.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

    private Long id;

    @NotNull(message = "User ID is mandatory")
    private Long userId;
    private String userName;
    private String userEmail;

    @NotNull(message = "Subscription Plan ID is mandatory")
    private Long planId;
    private  String planName;
    private String planCode;
    private Long price;
    private String currency;
    private  Integer maxBooksAllowed;
    private Integer maxDaysPerBook;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive= true;
    private  Boolean autoRenew;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private String notes;
    private  Long daysRemaining;
    private Boolean isValid;
    private Boolean isExpired;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
