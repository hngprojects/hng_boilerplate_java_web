package hng_java_boilerplate.organisation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.organisation.dto.CreateOrganisationRequestDto;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class e2e {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Test
    public void create_shouldReturn403_whenUserIsNotAuthenticated() throws Exception {
        // Arrange
        CreateOrganisationRequestDto orgRequest = new CreateOrganisationRequestDto(
                "New Org", "Description", "email@example.com", "Industry", "Type", "Country", "Address", "State"
        );

        mockMvc.perform(post("/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orgRequest)))
                .andExpect(status().isForbidden()); // Check for 403 Forbidden
    }

    @Test
    public void getOrganisationById_shouldReturnOrganisation_whenOrganisationExists() throws Exception {
        // Arrange
        Organisation organisation = new Organisation();
        organisation.setName("Test Org");
        organisation.setDescription("Description");
        organisation.setEmail("testorg@example.com");

        when(organisationRepository.findById(anyString())).thenReturn(java.util.Optional.of(organisation));

        mockMvc.perform(get("/api/v1/organisations/{organisationId}", organisation.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(organisation.getId()))
                .andExpect(jsonPath("$.name").value("Test Org"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.email").value("testorg@example.com"));
    }

    @Test
    public void getOrganisationById_shouldReturn404_whenOrganisationDoesNotExist() throws Exception {
        // Arrange
        String nonExistentOrganisationId = "nonexistent-id";

        when(organisationRepository.findById(anyString())).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/v1/organisations/{organisationId}", nonExistentOrganisationId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organization not found"));
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
