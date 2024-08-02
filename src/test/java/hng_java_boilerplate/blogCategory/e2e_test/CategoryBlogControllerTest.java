package hng_java_boilerplate.blogCategory.e2e_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryResponseDTO;
import hng_java_boilerplate.email.EmailServices.EmailProducerService;
import hng_java_boilerplate.newsletter.service.NewsletterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryBlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NewsletterService newsletterService;

    @MockBean
    private EmailProducerService emailProducerService;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateBlogCategorySuccess() throws Exception {

        CreateBlogCategoryRequestDTO requestDTO = new CreateBlogCategoryRequestDTO();
        requestDTO.setName("Tes Category");


        MvcResult result = mockMvc.perform(
                post("/api/v1/create/blog-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.name.name").value("Tes Category"))
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();
        CreateBlogCategoryResponseDTO responseDTO = objectMapper.readValue(responseContent, CreateBlogCategoryResponseDTO.class);
        assertEquals("Tes Category", responseDTO.getData().getName().getName());
    }
}