package com.library.library_backend.payload.request;

import com.library.library_backend.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationSearchRequest {
    private Long userId;

    private Long bookId;

    private ReservationStatus status;

    private Boolean activeOnly;

    private  int page=0;
    private  int size=20;

    private String sortBy="reservedAt";
    private String sortDirection="DESC";
}
