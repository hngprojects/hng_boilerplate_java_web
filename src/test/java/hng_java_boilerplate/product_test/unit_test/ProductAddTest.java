package hng_java_boilerplate.product_test.unit_test;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ProductAddTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ProductService productService = new ProductService() {
        @Override
        public Page<Product> productsSearch(String name, String category, Double minPrice, Double maxPrice, Pageable pageable) {
            return null;
        }

        @Override
        public Product addProduct(ProductDTO productDTO, "5") {
            return null;
        }
    };

    @Test
    void testAddProduct() {
        ProductDTO productDTO = new ProductDTO();
        Product product = new Product("ID", "Product Name", "Description", "Category", 100.0, "imageUrl", new User());

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(productDTO);

        assertEquals("Product Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals("Category", result.getCategory());
        assertEquals(100.0, result.getPrice());
        assertEquals("imageUrl", result.getImageUrl());
    }
}
