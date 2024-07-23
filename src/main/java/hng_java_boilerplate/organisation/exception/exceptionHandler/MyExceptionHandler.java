package hng_java_boilerplate.organisation.exception.exceptionHandler;

import hng_java_boilerplate.organisation.exception.InvitationValidationException;
import hng_java_boilerplate.organisation.exception.OrganisationException;
import hng_java_boilerplate.organisation.exception.response.ErrorResponse;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.InvalidClassException;

@ComponentScan
@ControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(InvitationValidationException.class)
    public ResponseEntity<?> invitationValidationHandler(InvitationValidationException invitationValidationException){
        ErrorResponse errorResponse = new ErrorResponse(
                invitationValidationException.getMessage(),
                invitationValidationException.getError(),
                invitationValidationException.getStatus()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrganisationException.class)
    public ResponseEntity<?> organisationException(OrganisationException organisationException){
        return new ResponseEntity<>(organisationException.getMessage(), HttpStatus.FORBIDDEN);
    }

}
