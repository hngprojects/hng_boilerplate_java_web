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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product();
        List<Product> productList = Arrays.asList(product);
        Page<Product> expectedProducts = new PageImpl<>(productList, pageable, productList.size());

        when(productRepository.searchProducts(name, category, minPrice, maxPrice, pageable))
                .thenReturn(expectedProducts);

        // Act
        Page<Product> result = productService.productsSearch(name, category, minPrice, maxPrice, pageable);

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
        Pageable pageable = PageRequest.of(0, 10);

        // Act & Assert
        ValidationError thrown = assertThrows(ValidationError.class, () -> {
            productService.productsSearch(name, category, minPrice, maxPrice, pageable);
        });

        // Verify the error details
        ErrorDTO errorDTO = thrown.getError();
        assertEquals("name is a required parameter", errorDTO.getMessage());
        assertEquals("name", errorDTO.getParameter());
    }

    @Test
    public void testProductsSearch_Exception_ReturnsEmptyPage() {
        // Arrange
        String name = "testProduct";
        String category = "testCategory";
        Double minPrice = 10.0;
        Double maxPrice = 100.0;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> emptyPage = Page.empty(pageable);

        when(productRepository.searchProducts(name, category, minPrice, maxPrice, pageable))
                .thenReturn(emptyPage);

        // Act
        Page<Product> result = productService.productsSearch(name, category, minPrice, maxPrice, pageable);

        // Assert
        assertEquals(emptyPage, result);
    }
}

