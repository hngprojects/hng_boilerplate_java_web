package hng_java_boilerplate.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusRequestDto {

    @NotBlank(message = "product id is required")
    private String productId;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotBlank(message = "Product Category is required")
    private String category;


    @DecimalMin(value = "1.00", message = "Price cannot be zero")
    private double price;

    private String imageUrl;
    private Boolean availabilityStatus;
}
