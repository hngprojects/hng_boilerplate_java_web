package hng_java_boilerplate.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStockDTO {
    private String productId;
    private double currentStock;
    private LocalDateTime lastUpdated;
}
