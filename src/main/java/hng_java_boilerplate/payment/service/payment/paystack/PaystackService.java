package hng_java_boilerplate.payment.service.payment.paystack;

import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.SubscriptionPlanRequest;
import org.springframework.http.ResponseEntity;

public interface PaystackService {

    ResponseEntity<?> initiatePayment(PaymentRequest request) throws JsonProcessingException;

    ResponseEntity<?> verifyPayment(String reference);

    ResponseEntity<?> createSubscriptionPlan(SubscriptionPlanRequest request);
}
