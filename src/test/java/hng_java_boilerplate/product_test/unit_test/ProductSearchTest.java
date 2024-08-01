package hng_java_boilerplate.product_test.unit_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ErrorDTO;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.exceptions.AuthenticationFailedException;
import hng_java_boilerplate.product.exceptions.RecordNotFoundException;
import hng_java_boilerplate.product.exceptions.ValidationError;
import hng_java_boilerplate.product.utils.GeneralResponseEntity;
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

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProductSearchTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductServiceImpl productService;

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

    // Create Product
    @Test
    void testCreateProduct() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        User mockUser = mock(User.class);

        UsernamePasswordAuthenticationToken mockAuthToken = mock(UsernamePasswordAuthenticationToken.class);
        when(mockAuthToken.getPrincipal()).thenReturn(mockUser);

        Product mockProduct = new Product();
        when(productService.createProduct(productDTO, mockAuthToken)).thenReturn(mockProduct);
        ResponseEntity<GeneralResponseEntity<Product>> responseEntity = productController.createProduct(productDTO, mockAuthToken);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockProduct, responseEntity.getBody().getInfo());
        assertEquals("Product created successfully", responseEntity.getBody().getMessage());
        verify(productService, times(1)).createProduct(productDTO, mockAuthToken);
    }

    @Test
    public void testUpdateProduct_Success(){

        String productId = "productId";
        ProductDTO productDTO = new ProductDTO();
        Product updatedProduct = new Product();
        User user = new User();
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(productService.updateProduct(productDTO, productId, principal)).thenReturn(updatedProduct);
        ResponseEntity<GeneralResponseEntity<Product>> response = productController.updateProduct(productDTO, productId, principal);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduct, response.getBody().getInfo());
        assertEquals("Product updated successfully", response.getBody().getMessage());
        verify(productService).updateProduct(productDTO, productId, principal);
    }

    @Test
    public void testUpdateProduct_NotFound() throws Exception {

        String productId = "productId";
        ProductDTO productDTO = new ProductDTO();
        User user = new User();
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        when(productService.updateProduct(productDTO, productId, principal)).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class, () -> productController.updateProduct(productDTO, productId, principal));
    }

    @Test
    public void testUpdateProduct_AuthenticationFailed() throws Exception {

        String productId = "productId";
        ProductDTO productDTO = new ProductDTO();
        User user = new User();
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        when(productService.updateProduct(productDTO, productId, principal)).thenThrow(AuthenticationFailedException.class);
        assertThrows(AuthenticationFailedException.class, () -> productController.updateProduct(productDTO, productId, principal));
    }


    @Test
    public void testGetProductById_Success(){

        String productId = "productId";
        Product product = new Product();

        when(productService.getProductById(productId)).thenReturn(product);
        ResponseEntity<GeneralResponseEntity<Product>> response = productController.getProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody().getInfo());
    }

    @Test
    public void testGetProductById_NotFound() {

        String productId = "productId";
        when(productService.getProductById(productId)).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class, () -> productController.getProduct(productId));
    }

    @Test
    public void testDeleteProduct_Success() throws Exception {
        String productId = "product-id";

        Mockito.doNothing().when(productService).deleteProduct(productId);
        ResponseEntity<GeneralResponseEntity<Product>> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

