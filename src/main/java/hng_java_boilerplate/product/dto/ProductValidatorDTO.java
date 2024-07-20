package hng_java_boilerplate.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductValidatorDTO {
    private boolean success;
    private List<ErrorDTO> errors;
    private int status_code;
}
