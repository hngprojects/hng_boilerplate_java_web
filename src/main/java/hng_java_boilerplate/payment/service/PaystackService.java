package hng_java_boilerplate.payment.service;

import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import org.springframework.http.ResponseEntity;

public interface PaystackService {

    ResponseEntity<?> initiatePayment(PaymentRequest request);

    ResponseEntity<?> verifyPayment(String reference);

}
