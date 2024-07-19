package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@Service

public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice) {
        if (name == null || name.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage("name is a required parameter");
            errorDTO.setParameter("name");
            throw new ValidationError(errorDTO);
        }

            return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }
}
