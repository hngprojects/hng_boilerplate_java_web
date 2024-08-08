package hng_java_boilerplate.product.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequestDto {


    private String product_id;

    private String name;

    private String description;

    private String category;

    @DecimalMin(value = "1.00", message = "Price cannot be zero")
    private Double price;

    @DecimalMin(value = "0.00", message = "Price cannot be less zero")
    private Integer current_stock;

    private String image_Url;
    private Boolean available;
}
