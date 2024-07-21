package hng_java_boilerplate.product.dto;

import hng_java_boilerplate.product.entity.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public ProductResponseDto toProductResponseDto(Product product){
        return new ProductResponseDto(product.getName(), product.getDescription(), product.getUser());
    }
}
