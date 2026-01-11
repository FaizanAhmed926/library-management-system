package com.library.library_backend.payload.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionPlanDTO {

    private Long id;

    @NotNull(message = "Plan code is mandatory")
    private String planCode;

    @NotNull(message = "Plan name is mandatory")
    private String name;

    private String description;

    @NotNull(message = "Duration date is mandatory")
    @Positive(message = "Duration date must be positive")
    private Integer durationDays;

    @NotNull(message = "Price  is mandatory")
    @Positive(message = "Price must be positive")
    private Long price;

    private String currency;

    @NotNull(message = "Max Book Allowed is mandatory")
    @Positive(message = "Max Book Allowed must be positive")
    private  Integer maxBooksAllowed;

    @NotNull(message = "Max Days is mandatory")
    @Positive(message = "max Days must be positive")
    private  Integer maxDaysPerBook;

    private Integer displayOrder;

    private Boolean isActive;

    private Boolean isFeatured;

    private  String badgeText;

    private  String adminNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;
}
