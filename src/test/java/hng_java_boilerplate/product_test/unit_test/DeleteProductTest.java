package hng_java_boilerplate.product_test.unit_test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeleteProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId("prod-123");
        product.setName("Test Product");
    }

    @Test
    public void testDeleteProduct() {

        productService.deleteProduct(product);

        verify(productRepository, times(1)).delete(product);
    }

}