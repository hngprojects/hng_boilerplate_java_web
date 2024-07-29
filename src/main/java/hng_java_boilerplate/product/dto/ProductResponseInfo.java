package hng_java_boilerplate.product.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseInfo {
    private String name;
    private String description;
    private String category;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String id;
}
