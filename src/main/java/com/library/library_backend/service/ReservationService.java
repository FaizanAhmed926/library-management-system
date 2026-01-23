package com.library.library_backend.service;

import com.library.library_backend.payload.dto.ReservationDTO;
import com.library.library_backend.payload.request.ReservationRequest;
import com.library.library_backend.payload.request.ReservationSearchRequest;
import com.library.library_backend.payload.response.PageResponse;

public interface ReservationService {
    ReservationDTO createReservation(ReservationRequest reservationRequest) throws Exception;
    ReservationDTO createReservationForUser(ReservationRequest reservationRequest,Long userId) throws Exception;
    ReservationDTO cancelReservation(Long reservationId) throws Exception;
    ReservationDTO fulfillReservation(Long reservationId) throws Exception;
    PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest searchRequest) throws Exception;
    PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest searchRequest);
}
