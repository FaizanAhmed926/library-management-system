package com.library.library_backend.payload.response;

import com.library.library_backend.domain.PaymentGateway;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitiateResponse {

    private Long paymentId;

    private PaymentGateway gateway;

    private String transactionId;

    private String razorpayOrderId;

    private  Long amount;

    private String description;

    private String checkoutUrl;

    private  String message;

    private Boolean success;

}
