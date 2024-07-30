package hng_java_boilerplate.product.service;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductCreateDto;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
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
    public Product getProductStock(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    @Override
    public Product createProduct(ProductCreateDto productCreateDto, String user) {
        User currentUser = userRepository.findByEmail(user).orElse(null);

        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (productRepository.existsById(uuid));

        Product product = new Product();
        product.setId(uuid);
        product.setName(productCreateDto.getName());
        product.setDescription(productCreateDto.getDescription());
        product.setCategory(productCreateDto.getCategory());
        product.setPrice(productCreateDto.getPrice());
        product.setImageUrl(productCreateDto.getImageUrl());
        product.setUser(currentUser);
        return productRepository.save(product);
    }
}
