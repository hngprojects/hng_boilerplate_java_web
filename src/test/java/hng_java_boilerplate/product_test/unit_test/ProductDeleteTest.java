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
    public void testDeleteProduct_ValidId_Success(){
        String productId = "invalidProductId";
        Product product = new Product();
        doReturn(java.util.Optional.of(product)).when(productRepository).findById(productId);
        doNothing().when(productRepository).delete(product);
        productService.deleteProductById(productId);
        verify(productRepository,times(1)).delete(product);
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
    @Test
    public void testDeleteProduct_UnauthorizedAccess_ThrowsUnauthorizedAccessException(){
        String productId = "ValidProductId";
        Product product = new Product();
        doReturn(java.util.Optional.of(product)).when(productRepository).findById(productId);
        doThrow(new UnauthorizedAccessException("You do not have permission to delete this product")).when(productService).deleteProductById(productId);
        UnauthorizedAccessException thrown = assertThrows(UnauthorizedAccessException.class, () ->{
            productService.deleteProductById(productId);
        });
        assertEquals("You do not have permission to delete this product", thrown.getMessage());
    }
    @Test
    public void testDeleteProduct_Exception_ReturnsInternalServerError() {
        String productId = "ValidProductId";
        Product product = new Product();
        doReturn(java.util.Optional.of(product)).when(productRepository).findById(productId);
        doThrow(new RuntimeException("Database error")).when(productService).deleteProductById(productId);
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->{
            productService.deleteProductById(productId);
        });
        assertEquals("Database error", thrown.getMessage());
    }
    }

