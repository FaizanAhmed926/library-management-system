package com.library.library_backend.service;

import com.library.library_backend.payload.dto.PaymentDTO;
import com.library.library_backend.payload.request.PaymentInitiateRequest;
import com.library.library_backend.payload.request.PaymentVerifyRequest;
import com.library.library_backend.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PaymentService {
    PaymentInitiateResponse initiatePayment(PaymentInitiateRequest req) throws Exception;
    PaymentDTO verifyPayment(PaymentVerifyRequest req) throws Exception;
    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
