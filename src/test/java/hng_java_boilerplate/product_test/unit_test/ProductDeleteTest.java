package hng_java_boilerplate.product_test.unit_test;

import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.exceptions.UnauthorizedAccessException;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductDeleteTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDeleteProduct_ProductNotFound_ThrowsProductNotFoundException(){
        String productId = "invalidProductId";
        doReturn(java.util.Optional.empty()).when(productRepository).findById(productId);
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class, () ->{
            productService.deleteProductById(productId);
        });
        assertEquals("Product not found", thrown.getMessage());
    }
}
