package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ProductMapper;
import hng_java_boilerplate.product.dto.ProductResponseDto;
import hng_java_boilerplate.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import hng_java_boilerplate.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

/* 
public interface ProductService {

    //Method to Search for products with certain criteria, returns a list of products
    Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable);
}
*/
