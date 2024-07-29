package hng_java_boilerplate.product.errorhandler;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductErrorDTO;
import hng_java_boilerplate.product.dto.ProductValidatorDTO;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@ControllerAdvice
public class ProductErrorHandler {

    private final ProductErrorDTO productErrorDTO;

    public ProductErrorHandler(ProductErrorDTO productErrorDTO) {
        this.productErrorDTO = productErrorDTO;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(ValidationError v){
        ProductValidatorDTO dto = new ProductValidatorDTO();
        dto.setErrors(List.of(v.getError()));
        dto.setStatus_code(HttpStatus.UNPROCESSABLE_ENTITY.value());
        dto.setSuccess(false);
        return new ResponseEntity<>(dto, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(NoHandlerFoundException ex){
        productErrorDTO.setMessage("Resource not found");
        productErrorDTO.setStatus_code(HttpStatus.NOT_FOUND.value());
        productErrorDTO.setSuccess(false);
        return new ResponseEntity<>(productErrorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(ProductNotFoundException ex){
        productErrorDTO.setMessage(ex.getMessage());
        productErrorDTO.setStatus_code(HttpStatus.NOT_FOUND.value());
        productErrorDTO.setSuccess(false);
        return new ResponseEntity<>(productErrorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        ProductValidatorDTO dto = new ProductValidatorDTO();
        List<ErrorDTO> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();
        dto.setErrors(errors);
        dto.setStatus_code(HttpStatus.BAD_REQUEST.value());
        dto.setSuccess(false);
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
