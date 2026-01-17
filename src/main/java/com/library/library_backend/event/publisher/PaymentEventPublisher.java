package com.library.library_backend.event.publisher;

import com.library.library_backend.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishPaymentsuccessEvent(Payment payment){
        applicationEventPublisher.publishEvent(payment);
    }
}
