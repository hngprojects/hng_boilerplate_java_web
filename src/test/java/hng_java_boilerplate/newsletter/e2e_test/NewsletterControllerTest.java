package hng_java_boilerplate.newsletter.e2e_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.newsletter.dto.NewsletterEmailRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NewsletterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJoinNewsletter() throws Exception {
        NewsletterEmailRequestDto subscriptionEmailRequestDto = new NewsletterEmailRequestDto("test758@gmail.com");

        mockMvc.perform(post("/api/v1/pages/newsletter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionEmailRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testJoinNewsletter_existingEmail() throws Exception {
        NewsletterEmailRequestDto subscriptionEmailRequestDto = new NewsletterEmailRequestDto("test758@gmail.com");

        mockMvc.perform(post("/api/v1/pages/newsletter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionEmailRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetListOfNewsletterSubscribers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/pages/listOfNewslettersSubscribers"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}