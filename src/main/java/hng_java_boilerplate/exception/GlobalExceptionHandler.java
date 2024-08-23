package hng_java_boilerplate.exception;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.jobs.dto.ApiResponse;
import hng_java_boilerplate.squeeze.dto.ResponseMessageDto;
import hng_java_boilerplate.user.dto.response.ErrorResponse;
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
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ErrorResponseDto errorResponseDTO = new ErrorResponseDto();
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ValidationError handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error: ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ValidationError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "validation error", errors);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleBadRequest(BadRequestException ex) {
        return setResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNotFound(NotFoundException ex) {
        return setResponse(ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleExpiredJwtException(ExpiredJwtException ex) {
        return setResponse("The JWT token has expired", HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleInvalidJwtSignatureException(SignatureException ex) {
        return setResponse("The JWT token has expired", HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        return setResponse("You are not allowed to access this endpoint.", HttpStatus.FORBIDDEN.getReasonPhrase(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessageDto> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessageDto("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleConflict(ConflictException ex) {
        return setResponse(ex.getMessage(), HttpStatus.CONFLICT.getReasonPhrase(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponseDto handleAuthenticationException(AuthenticationException ex) {
        return setResponse("Unauthorized. Please log in.", HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(QrGenerationException.class)
    public ResponseEntity<ErrorResponse> handleQrGenerationException(QrGenerationException ex) {
        ErrorResponse errorResponse = new ErrorResponse("Server error", "Could not generate QR code", 500);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUnAuthorized(UnAuthorizedException ex) {
        return setResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
    }

    @ExceptionHandler(StripeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto stripeException(StripeException ex) {
        return setResponse("The JWT token has expired", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());

    }

    @ExceptionHandler(JsonSyntaxException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto  jsonSyntaxException(JsonSyntaxException ex) {
        return setResponse("Invalid json", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(SignatureVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto signatureVerificationException(SignatureVerificationException ex) {
        return setResponse("Invalid signature", HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return setResponse("Resource not found", HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value());
    }

    private ErrorResponseDto setResponse(String message, String error, int statusCode){
        errorResponseDTO.setError(error);
        errorResponseDTO.setMessage(message);
        errorResponseDTO.setStatus_code(statusCode);
        return errorResponseDTO;
    }
}
