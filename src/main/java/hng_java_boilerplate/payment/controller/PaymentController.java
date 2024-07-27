package hng_java_boilerplate.payment.controller;


import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.service.PaystackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaystackService paystackService;

    public PaymentController(PaystackService paystackService) {
        this.paystackService = paystackService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        return paystackService.initiatePayment(paymentRequest);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable String reference) {
        return paystackService.verifyPayment(reference);
    }

}
