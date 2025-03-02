package hng_java_boilerplate.payment.controller;

import com.stripe.exception.StripeException;
import hng_java_boilerplate.payment.dtos.PaymentRequestBody;
import hng_java_boilerplate.payment.dtos.SessionResponse;
import hng_java_boilerplate.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "Payment", description = "Controller for Payment")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("stripe/upgrade-plan")
    @Operation(summary = "Upgrade plan request")
    public ResponseEntity<SessionResponse> createSession(@RequestBody @Valid PaymentRequestBody todo) throws StripeException {
        return service.createSession(todo);
    }

    @PostMapping("webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload, HttpServletRequest request) throws StripeException {
        return service.handleWebhook(payload, request);
    }

    @GetMapping("stripe/status")
    @Operation(summary = "check stripe payment status")
    public ResponseEntity<?> returnStatus(@RequestParam("session_id") String id) throws StripeException {
        return service.returnStatus(id);
    }

    @GetMapping("stripe/cancel")
    @Operation(summary = "cancel stripe payment session")
    public ResponseEntity<?> cancelSession(@RequestParam("session_id") String id) throws StripeException {
        return service.cancelSession(id);
    }

}
