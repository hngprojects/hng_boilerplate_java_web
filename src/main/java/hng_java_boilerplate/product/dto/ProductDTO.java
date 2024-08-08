package hng_java_boilerplate.product.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String imageUrl;
    private Boolean status;
    private Integer current_stock;
}
