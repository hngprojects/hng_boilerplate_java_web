package hng_java_boilerplate.payment.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import org.springframework.http.ResponseEntity;


public interface PaymentService {

    ResponseEntity<?> initiatePayment(PaymentRequest request) throws JsonProcessingException;

    ResponseEntity<?> verifyPayment(String reference);

    ResponseEntity<?> verifyPayment(String reference, String status, String transactionId);



}
