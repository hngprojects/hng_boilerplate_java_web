package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ProductMapper;
import hng_java_boilerplate.product.dto.ProductResponseDto;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductService(ProductRepository productRepository, ProductMapper productMapper){
        this.productRepository =  productRepository;
        this.productMapper = productMapper;
    }

    public Optional<ProductResponseDto> findProductById(String id){
        return productRepository.findById(id).map(productMapper::toProductResponseDto);
    }
}
