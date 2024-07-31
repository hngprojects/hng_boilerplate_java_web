package hng_java_boilerplate.payment.service.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestPaymentService implements PaymentService{
    @Override
    public ResponseEntity<?> initiatePayment(PaymentRequest request) throws JsonProcessingException {
        return null;
    }

    @Override
    public ResponseEntity<?> verifyPayment(String reference) {
        return null;
    }

    @Override
    public ResponseEntity<?> verifyPayment(String reference, String status, String transactionId) {
        return null;
    }
}
