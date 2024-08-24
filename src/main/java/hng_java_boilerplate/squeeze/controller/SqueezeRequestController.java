package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.email.EmailServices.EmailProducerService;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/squeeze")
@Validated
@RequiredArgsConstructor
@Tag(name="Squeeze")
public class SqueezeRequestController {

    private final SqueezeRequestService service;
    private final EmailProducerService emailProducerService;

    @PostMapping
    public ResponseEntity<?> handleSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        service.saveSqueezeRequest(request);

        String to = request.getEmail();
        String subject = "Email Template Confirmation";
        String text = "This is your email template";
        emailProducerService.sendEmailMessage(to, subject, text);

        return ResponseEntity.ok().body(new ResponseMessageDto("You are all signed up!", HttpStatus.OK.value()));
    }

    @PutMapping
    public ResponseEntity<?> updateSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        try {
            service.updateSqueezeRequest(request);
            return ResponseEntity.ok().body(new ResponseMessageDto("Your record has been successfully updated. You cannot update it again.", HttpStatus.OK.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessageDto(e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessageDto(e.getMessage(), HttpStatus.FORBIDDEN.value()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllSqueezeRequests() {
        List<SqueezeRequest> squeezeRequests = service.getAllSqueezeRequests();
        return ResponseEntity.ok().body(squeezeRequests);
    }

}