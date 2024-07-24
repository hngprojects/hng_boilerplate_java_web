package hng_java_boilerplate.product.dto;

import hng_java_boilerplate.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductSearchDTO {
    private boolean success;
    private int status_code;
    private List<ProductDTO> products;
    private int total;
    private int page;
    private int limit;
}
