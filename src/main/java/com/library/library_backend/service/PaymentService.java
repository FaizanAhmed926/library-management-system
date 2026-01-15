package com.library.library_backend.service;

import com.library.library_backend.payload.dto.PaymentDTO;
import com.library.library_backend.payload.request.PamentInitiateRequest;
import com.library.library_backend.payload.request.PaymentVerifyRequest;
import com.library.library_backend.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface PaymentService {
    PaymentInitiateResponse initiatePayment(PamentInitiateRequest req) throws Exception;
    PaymentDTO verifyPayment(PaymentVerifyRequest req);
    Page<PaymentDTO> getAllPayments(Pageable pageable);
}
