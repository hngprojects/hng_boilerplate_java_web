package hng_java_boilerplate.product.exceptions;

import hng_java_boilerplate.product.dto.ErrorDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ValidationError extends RuntimeException {
    private ErrorDTO error;

    public ValidationError(ErrorDTO error) {
        this.error = error;
    }
}
