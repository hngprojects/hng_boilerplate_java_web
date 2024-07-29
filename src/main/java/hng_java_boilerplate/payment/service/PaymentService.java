package hng_java_boilerplate.payment.service;

import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import org.springframework.http.ResponseEntity;


public interface PaymentService {

    ResponseEntity<?> initiatePayment(PaymentRequest request);

    ResponseEntity<?> verifyPayment(String reference);

    PaymentObjectResponse<?> getPaymentsByUserEmail(String email);

    PaymentObjectResponse<?> findPaymentByReference(String reference);

}
