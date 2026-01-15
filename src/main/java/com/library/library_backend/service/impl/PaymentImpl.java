package com.library.library_backend.service.impl;

import com.library.library_backend.domain.PaymentStatus;
import com.library.library_backend.model.Payment;
import com.library.library_backend.model.Subscription;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.dto.PaymentDTO;
import com.library.library_backend.payload.request.PamentInitiateRequest;
import com.library.library_backend.payload.request.PaymentVerifyRequest;
import com.library.library_backend.payload.response.PaymentInitiateResponse;
import com.library.library_backend.repository.PaymentRepository;
import com.library.library_backend.repository.SubscriptionRepository;
import com.library.library_backend.repository.UserRepository;
import com.library.library_backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentImpl implements PaymentService {

    private final UserRepository userRepository;
    private  final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentInitiateResponse initiatePayment(PamentInitiateRequest request) throws Exception {
        User user=userRepository.findById(request.getUserId()).get();
        Payment payment=new Payment();
        payment.setUser(user);
        payment.setPaymentType(request.getPaymentType());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN_"+ UUID.randomUUID());
        payment.setInitiatedAt(LocalDateTime.now());
        if (request.getSubscriptionId()!=null){
            Subscription sub=subscriptionRepository.findById(request.getSubscriptionId()).orElseThrow(()->new Exception("Subscription not found"));
            payment.setSubscription(sub);
        }
        payment=paymentRepository.save(payment);
        return null;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest req) {
        return null;
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return null;
    }
}
