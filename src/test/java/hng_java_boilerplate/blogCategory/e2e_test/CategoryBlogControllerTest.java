package hng_java_boilerplate.blogCategory.e2e_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.blogCategory.dto.CreateBlogCategoryRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryBlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "omoyibo@gmail.com", roles = "ADMIN")
    public void testCreateBlogCategory() throws Exception {

        CreateBlogCategoryRequestDTO requestDTO = new CreateBlogCategoryRequestDTO();
        requestDTO.setName("love Category");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/create/blog-categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name.name").value("love Category"));
    }
}