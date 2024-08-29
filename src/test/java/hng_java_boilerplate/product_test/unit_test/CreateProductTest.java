package hng_java_boilerplate.product_test.unit_test;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.product.dto.ProductDTO;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CreateProductTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {

        Organisation organisation = new Organisation();
        organisation.setId(UUID.randomUUID().toString());
        organisation.setName("Test Organisation");

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");

        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName("Test Product");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Test Product");


        ProductServiceImpl productServiceSpy = spy(productService);
        doReturn(product).when(productServiceSpy).mapToProduct(productDTO, organisation, user);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productServiceSpy.createProduct(user, organisation, productDTO);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }
}
