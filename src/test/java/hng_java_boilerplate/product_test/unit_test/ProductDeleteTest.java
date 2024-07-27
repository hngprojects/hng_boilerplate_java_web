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
import org.springframework.security.core.GrantedAuthority;
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
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }
//    @Test
//    public void testDeleteProduct_ValidId_Success(){
//        String productId = "invalidProductId";
//        Product product = new Product();
//        doReturn(java.util.Optional.of(product)).when(productRepository).findById(productId);
//        doNothing().when(productRepository).delete(product);
//        productService.deleteProductById(productId);
//        verify(productRepository,times(1)).delete(product);
//    }
    @Test
    public void testDeleteProduct_ProductNotFound_ThrowsProductNotFoundException(){
        String productId = "invalidProductId";
        doReturn(java.util.Optional.empty()).when(productRepository).findById(productId);
        ProductNotFoundException thrown = assertThrows(ProductNotFoundException.class, () ->{
            productService.deleteProductById(productId);
        });
        assertEquals("Product not found", thrown.getMessage());
    }
//    @Test
//    public void testDeleteProduct_UnauthorizedAccess_ThrowsUnauthorizedAccessException(){
//        String productId = "someProductId";
//        Product product = new Product();
//        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
//
//        // Mocking SecurityContext and Authentication
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.isAuthenticated()).thenReturn(true);
//
//        // Mocking GrantedAuthority
//        GrantedAuthority authority = new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return "ROLE_ADMIN";
//            }
//        };
//        when(authentication.getAuthorities()).thenReturn(Collections.singletonList(authority));
//
//        // Call the method under test
//        productService.deleteProductById(productId);
//
//        // Verify that the product was deleted
//        verify(productRepository).delete(product);
//    }

//    @Test
//    public void testDeleteProduct_Exception_ReturnsInternalServerError() {
//        String productId = "ValidProductId";
//        Product product = new Product();
//        doReturn(java.util.Optional.of(product)).when(productRepository).findById(productId);
//        doThrow(new RuntimeException("Database error")).when(productService).deleteProductById(productId);
//        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
//            productService.deleteProductById(productId);
//        });
//        assertEquals("Database error", thrown.getMessage());
//    }
}


