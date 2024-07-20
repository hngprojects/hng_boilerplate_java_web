package hng_java_boilerplate.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductErrorDTO {
    private boolean success;
    private String message;
    private int status_code;
}
