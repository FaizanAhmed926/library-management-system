package com.library.library_backend.service.gateway;

import com.library.library_backend.model.Payment;
import com.library.library_backend.model.User;
import com.library.library_backend.payload.response.PaymentLinkResponse;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {
    @Value("${razorpay.key.id:}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret:}")
    private String razorpayKeySecret;

    public PaymentLinkResponse createPaymentLink(User user, Payment payment){
        try {

            RazorpayClient razorpayClient=new RazorpayClient(razorpayKeyId,razorpayKeySecret);
            Long amountInPaisa=payment.getAmount()*(new java.math.BigDecimal("100")).intValue();
            JSONObject paymentLinkRequest=new JSONObject();
            paymentLinkRequest.put("amount",amountInPaisa);
            paymentLinkRequest.put("currency","INR");
            paymentLinkRequest.put("description",payment.getDescription());

            JSONObject customer=new JSONObject();
            customer.put("name",user.getFullName());
            customer.put("email",user.getEmail());
            if (user.getPhone() != null){
                customer.put("contact",user.getPhone());
            }
            paymentLinkRequest.put("customer",customer);

            JSONObject notify=new JSONObject();
            notify.put("email",true);
            notify.put("sms",user.getPhone()!=null);
            paymentLinkRequest.put("notify",notify);

            paymentLinkRequest.put("reminder_enable",true);
        }catch (RazorpayException e){
         throw  new RuntimeException(e);
        }
        return null;
    }
}
