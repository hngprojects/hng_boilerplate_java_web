package hng_java_boilerplate.exception;


import hng_java_boilerplate.blogCategory.exception.CategoryAlreadyExistsException;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.email.exception.EmailTemplateExists;
import hng_java_boilerplate.email.exception.EmailTemplateNotFound;
import hng_java_boilerplate.helpCenter.topic.exceptions.ResourceNotFoundException;
import hng_java_boilerplate.payment.exceptions.PaymentNotFoundException;
import hng_java_boilerplate.plans.exceptions.DuplicatePlanException;

import hng_java_boilerplate.product.errorhandler.ProductNotFoundException;

import hng_java_boilerplate.plans.exceptions.PlanNotFoundException;

import hng_java_boilerplate.resources.exception.ResourcesNotFoundException;
import hng_java_boilerplate.squeeze.exceptions.DuplicateEmailException;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import hng_java_boilerplate.twofactor.exception.InvalidTotpException;
import hng_java_boilerplate.user.dto.response.ErrorResponse;
import hng_java_boilerplate.user.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(),"Invalid request", HttpStatus.UNPROCESSABLE_ENTITY.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(),"Bad request", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(),"Bad request", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FacebookOAuthException.class)
    public ResponseEntity<ErrorResponse> handleFacebookOAuthException (FacebookOAuthException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnauthorizedUserException(SignatureException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized Access", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),"Bad request", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError handleBadRequest(BadRequestException ex) {
        return new CustomError(400, ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomError handleNotFound(NotFoundException ex) {
        return new CustomError(404, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ResponseMessageDto> handleDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ErrorResponse errorResponse = new ErrorResponse( "The JWT token has expired", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleInvalidJwtSignatureException(SignatureException ex) {
        ErrorResponse errorResponse = new ErrorResponse("The JWT signature is invalid", ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(DuplicatePlanException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseMessageDto> handleDuplicatePlanException(DuplicatePlanException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse("You are not allowed to access this endpoint.", ex.getMessage(), HttpStatus.FORBIDDEN.value());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDto> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageDto("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("This resource does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailTemplateNotFound.class)
    public ResponseEntity<?> emailTemplateNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("This email template does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailTemplateExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseMessageDto> handleDuplicateEmailTemplateException(EmailTemplateExists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Unauthorized", "Unauthorized. Please log in.", 401);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }


    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(),"Category Already Created", HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse( ex.getMessage(),"An error occurred while trying to update product status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(QrGenerationException.class)
    public ResponseEntity<ErrorResponse> handleQrGenerationException(QrGenerationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Server error", "Could not generate QR code", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(InvalidTotpException.class)
    public ResponseEntity<ResponseMessageDto> handleInvalidTotpException(InvalidTotpException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessageDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
    }

    @ExceptionHandler(PlanNotFoundException.class)
    public ResponseEntity<ErrorResponse> planNotFoundException(PlanNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("This pricing plan does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> stripeException(StripeException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Stripe error", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JsonSyntaxException.class)
    public ResponseEntity<ErrorResponse> jsonSyntaxException(JsonSyntaxException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid json", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<ErrorResponse> signatureVerificationException(SignatureVerificationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid signature", ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> paymentNotFoundException(PlanNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Payment not found", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourcesNotFoundException.class)
    public ResponseEntity<?> resourcesNotFoundException(ResourcesNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse("This resource does not exist", ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



}
