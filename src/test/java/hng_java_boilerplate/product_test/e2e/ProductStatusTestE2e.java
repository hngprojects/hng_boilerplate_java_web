package hng_java_boilerplate.product_test.e2e;

import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ProductStatusRequestDto;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductStatusTestE2e {

    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductServiceImpl productServiceImpl;
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
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProductStatus_Success() throws Exception {
        ProductStatusRequestDto requestDto = new ProductStatusRequestDto();
        requestDto.setProductId("123");
        requestDto.setName("Product Name");
        requestDto.setDescription("Description");
        requestDto.setCategory("Category");
        requestDto.setPrice(20.00);
        requestDto.setImageUrl("http://example.com/image.jpg");
        requestDto.setAvailabilityStatus(true);


        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/products/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateProductStatus_Failure_InvalidRequest() throws Exception {
        ProductStatusRequestDto requestDto = new ProductStatusRequestDto();
        requestDto.setProductId("123");
        requestDto.setName("Product Name");
        requestDto.setDescription("Description");
        requestDto.setCategory("Category");
        requestDto.setPrice(20.00);
        requestDto.setImageUrl("http://example.com/image.jpg");
        requestDto.setAvailabilityStatus(true);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/products/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }
}
