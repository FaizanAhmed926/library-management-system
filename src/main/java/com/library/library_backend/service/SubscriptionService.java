package com.library.library_backend.service;

import com.library.library_backend.exception.SubscriptionException;
import com.library.library_backend.payload.dto.SubscriptionDTO;
import com.library.library_backend.payload.response.PaymentInitiateResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO) throws Exception;
    SubscriptionDTO getUsersActiveSubscription(Long userId) throws Exception;
    SubscriptionDTO cancelSubscription(Long subscriptionId,String reason) throws SubscriptionException;
    SubscriptionDTO activateSubscription(Long subscriptionId,Long paymentId) throws SubscriptionException;
    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);
    void deactivateExpiredSubscriptions() throws Exception;
}
