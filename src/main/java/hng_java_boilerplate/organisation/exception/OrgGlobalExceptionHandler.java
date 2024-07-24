package hng_java_boilerplate.organisation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class OrgGlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ValidationError> errors = getAllValidationErrors(ex.getBindingResult());

        ExceptionResponse errorResponse = new ExceptionResponse(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(OrganisationNameAlreadyExistsException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            OrganisationNameAlreadyExistsException ex
    ) {
        var authErr = new AuthErrorResponse(
                "Bad request",
                ex.getMessage(),
                400
        );
        return new ResponseEntity<>(
                authErr,
                HttpStatus.BAD_REQUEST
        );
    }

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
