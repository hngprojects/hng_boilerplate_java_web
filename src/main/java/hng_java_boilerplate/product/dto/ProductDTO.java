package hng_java_boilerplate.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private Double price;
    private String image_url;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
