package hng_java_boilerplate.job.Exceptions;

// GlobalExceptionHandler.java
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import hng_java_boilerplate.job.models.ResponseWrapper;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseWrapper<Void>> resourceNotFoundException(ResourceNotFoundException ex) {
        ResponseWrapper<Void> errorResponse = new ResponseWrapper<>("404", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Add more exception handlers as needed
}


