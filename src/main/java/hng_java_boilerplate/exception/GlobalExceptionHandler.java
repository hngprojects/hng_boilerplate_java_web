package hng_java_boilerplate.exception;

import hng_java_boilerplate.user.response.LogInResponse;
import hng_java_boilerplate.user.response.UnsuccesfulResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<UnsuccesfulResponse> handleValidationExceptions(MethodArgumentNotValidException ex){
//        public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex){

        UnsuccesfulResponse unsuccesfulResponse = new UnsuccesfulResponse("Invalid request parameters", "Email and password are required.");
        return new ResponseEntity<>(unsuccesfulResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<UnsuccesfulResponse> handleEmailAlreadyExistsException(InvalidCredentialsException e) {
        UnsuccesfulResponse unsuccesfulResponse = new UnsuccesfulResponse("Authentication failed", e.getMessage());
        return new ResponseEntity<>(unsuccesfulResponse, HttpStatus.UNAUTHORIZED);
    }
}
