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
    private double price;
    private String imageUrl;
}
