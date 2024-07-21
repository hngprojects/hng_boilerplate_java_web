package hng_java_boilerplate.product.errorhandler;

import hng_java_boilerplate.product.dto.ProductValidatorDTO;
import hng_java_boilerplate.product.exceptions.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ProductErrorHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleException(ValidationError v){
        ProductValidatorDTO dto = new ProductValidatorDTO();
        dto.setErrors(List.of(v.getError()));
        dto.setStatus_code(HttpStatus.UNPROCESSABLE_ENTITY.value());
        dto.setSuccess(false);
        return new ResponseEntity<>(dto, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
