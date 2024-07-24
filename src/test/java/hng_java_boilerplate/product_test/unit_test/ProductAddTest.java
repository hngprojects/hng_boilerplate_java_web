package hng_java_boilerplate.product_test.unit_test;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductService;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ProductAddTest {

    private ProductRepository productRepository;
    private UserRepository userRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        userRepository = Mockito.mock(UserRepository.class);
        productService = new ProductServiceImpl(productRepository, userRepository);
    }

    @Test
    void testAddProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("ID");
        productDTO.setName("Product Name");
        productDTO.setDescription("Description");
        productDTO.setCategory("Category");
        productDTO.setPrice(100.0);
        productDTO.setImageUrl("imageUrl");

        User user = new User();
        Product product = new Product();
        product.setId("ID");
        product.setName("Product Name");
        product.setDescription("Description");
        product.setCategory("Category");
        product.setPrice(100.0);
        product.setImageUrl("imageUrl");
        product.setUser(user);

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.addProduct(productDTO, "5");

        assertEquals("Product Name", result.getName());
        assertEquals("Description", result.getDescription());
        assertEquals("Category", result.getCategory());
        assertEquals(100.0, result.getPrice());
        assertEquals("imageUrl", result.getImageUrl());
    }

    @Test
    void testAddProduct_UserNotFound() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("ID");
        productDTO.setName("Product Name");
        productDTO.setDescription("Description");
        productDTO.setCategory("Category");
        productDTO.setPrice(100.0);
        productDTO.setImageUrl("imageUrl");

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> productService.addProduct(productDTO, "5"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testAddProduct_SaveFailure() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId("ID");
        productDTO.setName("Product Name");
        productDTO.setDescription("Description");
        productDTO.setCategory("Category");
        productDTO.setPrice(100.0);
        productDTO.setImageUrl("imageUrl");

        User user = new User();

        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () ->  productService.addProduct(productDTO, "5"));

        assertEquals("Database error", exception.getMessage());
    }
}
