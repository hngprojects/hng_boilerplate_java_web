package hng_java_boilerplate.product.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GetProductsDTO(int status_code, String status, List<ProductDTO> data) {
}
