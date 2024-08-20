package hng_java_boilerplate.exception.exception_handler;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.email.exception.EmailTemplateExists;
import hng_java_boilerplate.email.exception.EmailTemplateNotFound;
import hng_java_boilerplate.exception.exception_class.*;
import hng_java_boilerplate.exception.exception_dto.ErrorResponse;
import hng_java_boilerplate.exception.exception_dto.ValidationError;
import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.payment.exceptions.PaymentNotFoundException;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;
import hng_java_boilerplate.plans.exceptions.PlanNotFoundException;
import hng_java_boilerplate.resources.exception.ResourcesNotFoundException;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import hng_java_boilerplate.twofactor.exception.InvalidTotpException;
import hng_java_boilerplate.user.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.UnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(BadRequestException ex) {
        return new ErrorResponse(400, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException ex) {
        return new ErrorResponse(404, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidationError handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error: ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ValidationError(422, "validation error", errors);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorized(UnauthorizedException ex) {
        return new ErrorResponse(401, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ErrorResponse error = new ErrorResponse(400, "Total video size should not be more than 50mb");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleUnavailable(UnavailableException ex) {
        return new ErrorResponse(503, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestPartException(MissingServletRequestPartException ex){
       ErrorResponse errorResponse= new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ErrorResponse errorResponse = new ErrorResponse(400, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServer(InternalServerException  ex) {
        return new ErrorResponse(500, ex.getMessage());
    }


    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse( ex.getMessage(),"Invalid request", HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse( ex.getMessage(),"Bad request", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse( ex.getMessage(),"Bad request", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FacebookOAuthException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleFacebookOAuthException (FacebookOAuthException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleUnauthorizedUserException(SignatureException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Unauthorized Access", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ResponseMessageDto> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse( "The JWT token has expired", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleInvalidJwtSignatureException(SignatureException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("The JWT signature is invalid", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DuplicatePlanException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseMessageDto> handleDuplicatePlanException(DuplicatePlanException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("You are not allowed to access this endpoint.", ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("This resource does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailTemplateNotFound.class)
    public ResponseEntity<?> emailTemplateNotFoundException(ResourceNotFoundException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("This email template does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailTemplateExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseMessageDto> handleDuplicateEmailTemplateException(EmailTemplateExists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Unauthorized", "Unauthorized. Please log in.", 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(QrGenerationException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> handleQrGenerationException(QrGenerationException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Server error", "Could not generate QR code", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(InvalidTotpException.class)
    public ResponseEntity<ResponseMessageDto> handleInvalidTotpException(InvalidTotpException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> planNotFoundException(PlanNotFoundException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("This pricing plan does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> stripeException(StripeException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Stripe error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> jsonSyntaxException(JsonSyntaxException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Invalid json", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> signatureVerificationException(SignatureVerificationException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Invalid signature", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<hng_java_boilerplate.user.dto.response.ErrorResponse> paymentNotFoundException(PlanNotFoundException ex) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("Payment not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<?> resourcesNotFoundException(ResourcesNotFoundException ex, WebRequest request) {
        hng_java_boilerplate.user.dto.response.ErrorResponse errorResponse = new hng_java_boilerplate.user.dto.response.ErrorResponse("This resource does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDto> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageDto("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }
}
