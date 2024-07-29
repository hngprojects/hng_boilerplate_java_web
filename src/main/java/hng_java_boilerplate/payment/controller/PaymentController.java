package hng_java_boilerplate.payment.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.service.payment.PaymentService;
import hng_java_boilerplate.payment.service.payment.PaymentServiceFactory;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    private final UserService userService;

    private PaymentServiceFactory paymentServiceFactory;


    @Autowired
    public PaymentController(@Qualifier("paystackService") PaymentService paymentService, UserService userService, PaymentServiceFactory paymentServiceFactory) {
        this.paymentService = paymentService;
        this.userService = userService;
        this.paymentServiceFactory = paymentServiceFactory;
    }


    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) throws JsonProcessingException {
        PaymentService paymentService = paymentServiceFactory.getPaymentService(paymentRequest.getProvider());
        return paymentService.initiatePayment(paymentRequest);

    }


    @GetMapping("/verify/paystack")
    public ResponseEntity<?> verifyPaystackPayment(@RequestParam String reference) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService("paystack");
        return paymentService.verifyPayment(reference);
    }



    @GetMapping("/verify/flutterwave")
    public ResponseEntity<?> verifyFlutterwavePayment(
            @RequestParam String reference,
            @RequestParam String status,
            @RequestParam String transactionId) {
        PaymentService paymentService = paymentServiceFactory.getPaymentService("flutterwave");
        return paymentService.verifyPayment(reference, status, transactionId);
    }





}
