package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;
    private UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable) {
        if (name == null || name.isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage("name is a required parameter");
            errorDTO.setParameter("name");
            throw new ValidationError(errorDTO);
        }
        return productRepository.searchProducts(name, category, minPrice, maxPrice, pageable);
    }


    @Override
    public Product addProduct(ProductDTO productDTO, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setPrice(productDTO.getPrice());
        product.setImageUrl(productDTO.getImageUrl());
        product.setUser(user);

        return productRepository.save(product);
    }
}
