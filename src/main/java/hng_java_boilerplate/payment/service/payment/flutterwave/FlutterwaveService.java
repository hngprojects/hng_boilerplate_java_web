package hng_java_boilerplate.payment.service.payment.flutterwave;

import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.CardPaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.SubscriptionPlanRequest;
import org.springframework.http.ResponseEntity;

public interface FlutterwaveService {

    ResponseEntity<?> initiatePayment(PaymentRequest request) throws JsonProcessingException;

    ResponseEntity<?> verifyPayment(String reference, String status, String transactionId);

    ResponseEntity<?> createSubscriptionPlan(SubscriptionPlanRequest request) throws JsonProcessingException;

    public ResponseEntity<String> processCardPayment(CardPaymentRequest request);


}
