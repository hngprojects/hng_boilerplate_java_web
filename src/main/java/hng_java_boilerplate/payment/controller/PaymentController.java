package hng_java_boilerplate.payment.controller;


import hng_java_boilerplate.payment.dtos.reqests.PaymentRequest;
import hng_java_boilerplate.payment.dtos.responses.PaymentObjectResponse;
import hng_java_boilerplate.payment.service.PaymentService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {


    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    private final PaymentService paymentService;

    private final UserService userService;


    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.initiatePayment(paymentRequest);
    }

    @GetMapping("/verify/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable String reference) {
        return paymentService.verifyPayment(reference);
    }


    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPaymentsForUser() {
        User user = userService.getLoggedInUser();
        if (user == null) {
            var response = PaymentObjectResponse.builder().message("Unauthorized").status_code("401").build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        PaymentObjectResponse<?>  response = paymentService.getPaymentsByUserEmail(user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<?> getPaymentByReference(@PathVariable String reference) {
        PaymentObjectResponse<?> response = paymentService.findPaymentByReference(reference);
        if ("200".equals(response.getStatus_code())) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }



}
