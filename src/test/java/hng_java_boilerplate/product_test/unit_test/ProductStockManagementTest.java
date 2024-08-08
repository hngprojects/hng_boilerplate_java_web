package hng_java_boilerplate.product_test.unit_test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ProductUpdateResponseDto;
import hng_java_boilerplate.product.entity.Product;
import hng_java_boilerplate.product.errorhandler.ProductNotFoundException;
import hng_java_boilerplate.product.repository.ProductRepository;

import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductStockManagementTest {


    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductServiceImpl productServiceImpl;

    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testProductStockAvailable() throws Exception {

        String productId = "03202052-e1ad-4d2f-a56c-b5d0e99fe55a";

        String jsonRequest = objectMapper.writeValueAsString(productId);

        mockMvc.perform(get("/api/v1/products/03202052-e1ad-4d2f-a56c-b5d0e99fe55a/stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    public void testWithNoProductId() throws Exception {

        String productId = "";

        String jsonRequest = objectMapper.writeValueAsString(productId);

        mockMvc.perform(get("/api/v1/products//stock/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testAvailableProductStock_ProductFound() {

        String productId = "1";
        Product product = new Product();
        product.setId(productId);
        product.setCurrentStock(10);
        product.setUpdatedAt(LocalDateTime.now());
        when(productRepository.findById(any())).thenReturn(Optional.of(product));


        ProductUpdateResponseDto response = productService.availableProductStock(productId);


        assertEquals("success", response.getMessage());
        assertEquals(HttpStatus.OK.value(), response.getStatus_code());
        assertEquals(productId, response.getData().getProduct_Id());
        assertEquals(10, response.getData().getCurrent_Stock());
    }

    @Test
    public void testAvailableProductStock_ProductNotFound() {

        String productId = "1";
        when(productRepository.findById(any())).thenReturn(Optional.empty());


        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.availableProductStock(productId));
        assertEquals("Product With id: 1 Not Found", exception.getMessage());
    }


    @Test
    public void availableProductStock_withNullProductId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/products/ /stock")
                        .param("product_id", (String) null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request: Product Id cannot be null "));
    }

    @Test
    public void availableProductStock_withEmptyProductId_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/products/ /stock")
                        .param("product_id", ""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status_code").value(400))
                .andExpect(jsonPath("$.message").value("Bad Request: Product Id cannot be null "));
    }


}