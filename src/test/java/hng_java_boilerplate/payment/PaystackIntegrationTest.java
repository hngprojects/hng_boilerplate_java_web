package hng_java_boilerplate.payment;

import hng_java_boilerplate.payment.controller.PaymentController;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentInitializationResponse;
import hng_java_boilerplate.payment.dtos.responses.PaymentVerificationResponse;
import hng_java_boilerplate.payment.service.PaystackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PaystackIntegrationTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaystackService paystackService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitiatePayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000);

        PaymentInitializationResponse initializationResponse = new PaymentInitializationResponse();
        initializationResponse.setAuthorizationUrl("http://example.com/authorize");
        initializationResponse.setReference("test-reference");

        when(paystackService.initiatePayment(paymentRequest))
                .thenReturn(ResponseEntity.ok(initializationResponse));

        ResponseEntity<?> response = paymentController.initiatePayment(paymentRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(initializationResponse, response.getBody());
    }

    @Test
    public void testVerifyPayment() {
        String reference = "test-reference";

        PaymentVerificationResponse verificationResponse = new PaymentVerificationResponse();
        verificationResponse.setReference(reference);
        verificationResponse.setStatus("success");
        verificationResponse.setCurrency("NGN");
        verificationResponse.setChannel("card");
        verificationResponse.setPaid_at("2024-07-26T12:00:00");
        verificationResponse.setAmount("1000");

        when(paystackService.verifyPayment(reference))
                .thenReturn(ResponseEntity.ok(verificationResponse));

        ResponseEntity<?> response = paymentController.verifyPayment(reference);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(verificationResponse, response.getBody());
    }

}
