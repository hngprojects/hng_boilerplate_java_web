package hng_java_boilerplate.plans.configuration;

import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class PlanExceptionHandler {

    @ExceptionHandler(DuplicatePlanException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> duplicatePlan() {
        var response = new HashMap<>(){{
            put("status_code", 400);
            put("error", "Subscription plan already exists");
        }};
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
