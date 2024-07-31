package hng_java_boilerplate.payment.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.reqests.SubscriptionPlanRequest;
import hng_java_boilerplate.payment.service.payment.flutterwave.FlutterwaveService;
import hng_java_boilerplate.payment.service.payment.paystack.PaystackService;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final UserService userService;

    private final FlutterwaveService flutterwaveService;

    private final PaystackService paystackService;


    public PaymentController(UserService userService, FlutterwaveService flutterwaveService,
                             PaystackService paystackService) {
        this.userService = userService;
        this.flutterwaveService = flutterwaveService;
        this.paystackService = paystackService;
    }

    @PostMapping("/flutterwave")
    public ResponseEntity<?> createFlutterWaveSubscriptionPlan(@RequestBody SubscriptionPlanRequest request) throws JsonProcessingException {
        return flutterwaveService.createSubscriptionPlan(request);
    }


    @PostMapping("/paystack")
    public ResponseEntity<?> createPaystackSubscriptionPlan(@RequestBody SubscriptionPlanRequest request) throws JsonProcessingException {
        return paystackService.createSubscriptionPlan(request);
    }








}
