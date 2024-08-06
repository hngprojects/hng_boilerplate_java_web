package hng_java_boilerplate.product.exceptions;
import hng_java_boilerplate.product.utils.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<GeneralResponse<String>> recordNotFoundException(RecordNotFoundException exception){

        GeneralResponse<String> responseEntity = new GeneralResponse<>();
        responseEntity.setMessage(exception.message);
        return new ResponseEntity<>(responseEntity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GeneralAppException.class)
    public ResponseEntity<GeneralResponse<String>> generalAppException(GeneralAppException exception){
        GeneralResponse<String> responseEntity = new GeneralResponse<>();
        responseEntity.setMessage(exception.getMessage());
        return new ResponseEntity<>(responseEntity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
