package hng_java_boilerplate.payment.controller;

import com.stripe.exception.StripeException;
import hng_java_boilerplate.payment.dtos.PaymentRequestBody;
import hng_java_boilerplate.payment.dtos.SessionResponse;
import hng_java_boilerplate.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping("stripe/upgrade-plan")
    public ResponseEntity<SessionResponse> createSession(@RequestBody @Valid PaymentRequestBody todo) throws StripeException {
        return service.createSession(todo);
    }

    @PostMapping("webhook")
    public void handleWebhook(@RequestBody String payload, HttpServletRequest request) throws StripeException {
        service.handleWebhook(payload, request);
    }

    @GetMapping("stripe/status")
    public ResponseEntity<?> returnStatus(@RequestParam("session_id") String id) throws StripeException {
        return service.returnStatus(id);
    }

}
