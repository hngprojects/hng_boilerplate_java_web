package hng_java_boilerplate.product_test.unit_test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EditProductTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product existingProduct;
    private ProductDTO productDTO;
    private Product updatedProduct;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        existingProduct = new Product();
        existingProduct.setId("prod-123");
        existingProduct.setName("Old Product");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(100.0);
        existingProduct.setCategory("Old Category");
        existingProduct.setImageUrl("old-url");

        productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setDescription(null);
        productDTO.setPrice(150.0);
        productDTO.setCategory(null);
        productDTO.setImage_url("updated-url");

        updatedProduct = new Product();
        updatedProduct.setId("prod-123");
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Old Description");
        updatedProduct.setPrice(150.0);
        updatedProduct.setCategory("Old Category");
        updatedProduct.setImageUrl("updated-url");
    }

    @Test
    public void testEditProduct() {

        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO result = productService.editProduct(existingProduct, productDTO);

        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Old Description", result.getDescription());
        assertEquals(150.0, result.getPrice());
        assertEquals("Old Category", result.getCategory());
        assertEquals("updated-url", result.getImage_url());

        verify(productRepository, times(1)).save(existingProduct);
    }
}
