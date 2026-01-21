package com.library.library_backend.service;

import com.library.library_backend.domain.FineStatus;
import com.library.library_backend.domain.FineType;
import com.library.library_backend.payload.dto.FineDTO;
import com.library.library_backend.payload.request.CreateFineRequest;
import com.library.library_backend.payload.request.WaiveFineRequest;
import com.library.library_backend.payload.response.PageResponse;
import com.library.library_backend.payload.response.PaymentInitiateResponse;

import java.util.List;

public interface FineService {
    FineDTO createFine(CreateFineRequest createFineRequest) throws Exception;
    PaymentInitiateResponse payFine(Long fineId,String transactionId) throws Exception;
    void  markFineAsPaid(Long fineId, Long amount, String transactionid) throws Exception;
    FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception;
    List<FineDTO> getMyFine(FineStatus status,FineType type) throws Exception;
    PageResponse<FineDTO> getAllFine(FineStatus status, FineType type, Long userId, int page, int size);
}
