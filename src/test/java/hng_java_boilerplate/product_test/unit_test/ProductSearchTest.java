package hng_java_boilerplate.product_test.unit_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.exceptions.ValidationError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.entity.Product;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductSearchTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProductsSearch_ValidParameters() {
        // Arrange
        String name = "testProduct";
        String category = "testCategory";
        Double minPrice = 10.0;
        Double maxPrice = 100.0;
        Product product = new Product();
        List<Product> expectedProducts = Arrays.asList(product);

        when(productRepository.searchProducts(name, category, minPrice, maxPrice))
                .thenReturn(expectedProducts);

        // Act
        List<Product> result = productService.productsSearch(name, category, minPrice, maxPrice);

        // Assert
        assertEquals(expectedProducts, result);
    }

    @Test
    public void testProductsSearch_NoName_ThrowsValidationError() {
        // Arrange
        String name = "";
        String category = "testCategory";
        Double minPrice = 10.0;
        Double maxPrice = 100.0;

        // Act & Assert
        ValidationError thrown = assertThrows(ValidationError.class, () -> {
            productService.productsSearch(name, category, minPrice, maxPrice);
        });

        // Verify the error details
        ErrorDTO errorDTO = thrown.getError();
        assertEquals("name is a required parameter", errorDTO.getMessage());
        assertEquals("name", errorDTO.getParameter());
    }


    @Test
    public void testProductsSearch_Exception_ReturnsEmptyList() {
        // Arrange
        String name = "testProduct";
        String category = "testCategory";
        Double minPrice = 10.0;
        Double maxPrice = 100.0;

        when(productRepository.searchProducts(name, category, minPrice, maxPrice))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        List<Product> result = productService.productsSearch(name, category, minPrice, maxPrice);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }
}

