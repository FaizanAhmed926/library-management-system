package com.library.library_backend.event.listener;

import com.library.library_backend.exception.SubscriptionException;
import com.library.library_backend.model.Payment;
import com.library.library_backend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class paymentEventListener {
    private final SubscriptionService subscriptionService;

    @Async
    @EventListener
    @Transactional
    public void handlePaymentSuccess(Payment payment) throws SubscriptionException {
        switch (payment.getPaymentType()){
            case FINE :
            case LOST_BOOK_PENALTY:
            case DAMAGED_BOOK_PENALTY:
                break;
            case MEMBERSHIP:
                subscriptionService.activateSubscription(payment.getSubscription().getId(), payment.getId());
        }
    }
}
