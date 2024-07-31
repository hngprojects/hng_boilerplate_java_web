package hng_java_boilerplate.organisation.exception;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Order(1)
@RestControllerAdvice
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

    @ExceptionHandler(PermissionNameAlreadyExistsException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            PermissionNameAlreadyExistsException ex
    ) {
        System.out.println("Handling PermissionNameAlreadyExistsException: " + ex.getMessage());
        var authErr = new AuthErrorResponse(
                "Bad request",
                "Permission name already exists",
                409
        );
        return new ResponseEntity<>(
                authErr,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(OrganisationNotFoundException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            OrganisationNotFoundException ex
    ) {
        var authErr = new AuthErrorResponse(
                "Bad request",
                "Organisation not found",
                404
        );
        return new ResponseEntity<>(
                authErr,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(NotPermittedException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            NotPermittedException ex
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

    @ExceptionHandler(RoleNameAlreadyExistsException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            RoleNameAlreadyExistsException ex
    ) {
        var authErr = new AuthErrorResponse(
                "Bad request",
                "Role already exists",
                409
        );
        return new ResponseEntity<>(
                authErr,
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<AuthErrorResponse> handleEntityNotFoundExceptions(
            PermissionNotFoundException ex
    ) {
        var authErr = new AuthErrorResponse(
                "Bad request",
                "One or more permissions not found",
                404
        );
        return new ResponseEntity<>(
                authErr,
                HttpStatus.NOT_FOUND
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
