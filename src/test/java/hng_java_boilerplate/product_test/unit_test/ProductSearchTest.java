package hng_java_boilerplate.product_test.unit_test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.exceptions.AuthenticationFailedException;
import hng_java_boilerplate.product.exceptions.RecordNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.utils.GeneralResponse;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProductSearchTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductServiceImpl service;

    @InjectMocks
    private ProductController productController;

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

    @Test
    public void testGetProductById_Success(){

        String productId = "productId";
        Product product = new Product();

        when(service.getProductById(productId)).thenReturn(product);
        ResponseEntity<GeneralResponse<Product>> response = productController.getProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody().getInfo());
    }

    @Test
    public void testGetProductById_NotFound() {

        String productId = "productId";
        when(service.getProductById(productId)).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class, () -> productController.getProduct(productId));
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        String productId = "product-id";

        Mockito.doNothing().when(service).deleteProduct(productId);
        ResponseEntity<GeneralResponse<Product>> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteProduct_NotFound() {
        String productId = "productId";
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        ResponseEntity<GeneralResponse<Product>> response = productController.deleteProduct("jhjh");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}

