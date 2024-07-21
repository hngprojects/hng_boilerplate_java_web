package hng_java_boilerplate.squeeze.controller;

import hng_java_boilerplate.squeeze.entity.SqueezeRequest;
import hng_java_boilerplate.squeeze.service.SqueezeRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/squeeze")
@Validated
public class SqueezeRequestController {

    @Autowired
    private SqueezeRequestService service;

    @PostMapping
    public ResponseEntity<?> handleSqueezeRequest(@Valid @RequestBody SqueezeRequest request) {
        try {
            service.saveSqueezeRequest(request);
            return ResponseEntity.ok(new ResponseMessage("Your request has been received. You will get a template shortly."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Failed to submit your request"));
        }
    }

    public static class ResponseMessage {
        private String message;

        public ResponseMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
