package hng_java_boilerplate.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String description;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String category;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private Double price;

    @NotNull(message = "Name is required")
    @NotBlank(message = "Name is required")
    private String image_url;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
