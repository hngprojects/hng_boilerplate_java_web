package hng_java_boilerplate.SMS.SmsException;

import hng_java_boilerplate.util.ConstantMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SMSExceptionHandler {
    @ExceptionHandler(PhoneNumberOrMessageNotValidException.class)
    public ResponseEntity<?> phoneNumberOrMessageNotValidException(PhoneNumberOrMessageNotValidException exception){
        ExceptionResponse<?> exceptionResponse = new ExceptionResponse<>();
        exceptionResponse.setStatus(ConstantMessages.UNSUCCESSFUL.getMessage());
        exceptionResponse.setStatus_code(HttpStatus.BAD_REQUEST.value());
        exceptionResponse.setMessage(ConstantMessages.INVALID_PHONE_NUMBER_OR_CONTENT.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_GATEWAY);
    }
}
