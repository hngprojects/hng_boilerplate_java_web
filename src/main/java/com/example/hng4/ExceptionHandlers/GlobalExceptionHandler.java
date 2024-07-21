package com.example.hng4.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, Object> body = Map.of(
                "message", "Validation error",
                "error", ex.getMessage(),
                "statusCode", HttpStatus.BAD_REQUEST.value()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Add more exception handlers as needed
}
