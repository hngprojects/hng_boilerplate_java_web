package hng_java_boilerplate.organisation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class e2e {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create_shouldReturn403_whenUserIsNotAuthenticated() throws Exception {
        // Arrange
        CreateOrganisationRequestDto orgRequest = new CreateOrganisationRequestDto(
                "New Org",
                "Description",
                "email@example.com",
                "Industry",
                "Type",
                "Country",
                "Address",
                "State"
        );

        // Act & Assert
        mockMvc.perform(post("/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orgRequest)))
                .andExpect(status().isForbidden()); // Check for 403 Forbidden
    }

    // Helper method to convert object to JSON string
    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
