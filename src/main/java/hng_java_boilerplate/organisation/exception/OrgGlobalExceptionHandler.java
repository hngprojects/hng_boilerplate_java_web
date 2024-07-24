package hng_java_boilerplate.organisation.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class OrgGlobalExceptionHandler {

    private List<ValidationError> getAllValidationErrors(BindingResult bindingResult) {
        List<ValidationError> errors = new ArrayList<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.add(toValidationError(fieldError));
        }
        return errors;
    }
    private ValidationError toValidationError(FieldError fieldError) {
        return new ValidationError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
