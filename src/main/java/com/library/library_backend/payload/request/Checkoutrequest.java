package com.library.library_backend.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Checkoutrequest {
    @NotNull(message = "Book ID is mandatory")
    private Long bookId;

    @Min(value = 1,message = "Checkout days must be at least 1")
    private  Integer checkoutDays=14;

    private String notes;
}
