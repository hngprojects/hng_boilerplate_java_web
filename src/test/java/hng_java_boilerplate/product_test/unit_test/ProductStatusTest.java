package hng_java_boilerplate.product_test.unit_test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.product.controller.ProductController;
import hng_java_boilerplate.product.dto.ProductStatusRequestDto;
import hng_java_boilerplate.product.repository.ProductRepository;
import hng_java_boilerplate.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
public class ProductStatusTest {


    @InjectMocks
    private ProductController productController;


    @Mock
    private ProductServiceImpl productServiceImpl;

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
    void testUpdateProductStatus() throws Exception {

        ProductStatusRequestDto invalidRequestDto = new ProductStatusRequestDto();
        invalidRequestDto.setProductId("03202052-e1ad-4d2f-a56c-b5d0e99fe55a");
        invalidRequestDto.setName("Water");
        invalidRequestDto.setCategory("LIQUID");
        invalidRequestDto.setPrice(3.00);


        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);

        mockMvc.perform(patch("/api/v1/products/03202052-e1ad-4d2f-a56c-b5d0e99fe55a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateProductStatusWithInvalidRequest() throws Exception {

        ProductStatusRequestDto invalidRequestDto = new ProductStatusRequestDto();
        invalidRequestDto.setProductId(" ");
        invalidRequestDto.setName(" ");
        invalidRequestDto.setCategory("LIQUID");
        invalidRequestDto.setPrice(3.00);


        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);

        mockMvc.perform(patch("/api/v1/products/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}