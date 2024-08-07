package hng_java_boilerplate.product_test.unit_test;

import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductSearchDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.exceptions.ProductNotFoundException;
import hng_java_boilerplate.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetAllProductTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllProducts_Success(){
        Product product =  new Product();
        List<Product> products = Collections.singletonList(product);
        Page<Product> productPage = new PageImpl<>(products, PageRequest.of(0,10),1);
        when(productService.getAllProducts(any(PageRequest.class))).thenReturn(productPage);
        ResponseEntity<?> response = productController.getAllProducts(0,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductSearchDTO responseBody = (ProductSearchDTO) response.getBody();
        assertEquals(1,responseBody.getTotal());
        assertEquals(10,responseBody.getLimit());
        assertEquals(0,responseBody.getPage());
        assertEquals(true,responseBody.isSuccess());
    }
    @Test
    public void testGetAllProducts_NoContent() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(productService.getAllProducts(any(PageRequest.class))).thenReturn(productPage);

        // Act
        ResponseEntity<?> response = productController.getAllProducts(0, 10);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductSearchDTO responseBody = (ProductSearchDTO) response.getBody();
        assertEquals(0, responseBody.getTotal());
        assertEquals(10, responseBody.getLimit());
        assertEquals(0, responseBody.getPage());
        assertEquals(true, responseBody.isSuccess());
        assertEquals(Collections.emptyList(), responseBody.getProducts());
    }

    @Test
    public void testGetAllProducts_ProductNotFoundException() {
        // Arrange
        when(productService.getAllProducts(any(PageRequest.class))).thenThrow(new ProductNotFoundException("Product not found"));

        // Act
        ResponseEntity<?> response = productController.getAllProducts( 0, 10);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDTO responseBody = (ErrorDTO) response.getBody();
        assertEquals("Product not found", responseBody.getMessage());
    }

    @Test
    public void testGetAllProducts_InternalServerError() {
        // Arrange
        when(productService.getAllProducts(any(PageRequest.class))).thenThrow(new RuntimeException("Internal Server Error"));

        // Act
        ResponseEntity<?> response = productController.getAllProducts( 0, 10);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDTO responseBody = (ErrorDTO) response.getBody();
        assertEquals("Internal Server Error", responseBody.getMessage());
    }
}
