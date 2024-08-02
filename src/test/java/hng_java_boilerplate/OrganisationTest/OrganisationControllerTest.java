package hng_java_boilerplate.OrganisationTest;

import hng_java_boilerplate.organisation.controller.OrganisationController;
import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.service.OrganisationServices;
import hng_java_boilerplate.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganisationController.class)
class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganisationServices organisationServices;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setName("testUser");
    }

    @Test
    @WithMockUser
    void createOrganisation_Success() throws Exception {
        CreateOrganisationDTO dto = new CreateOrganisationDTO();
        dto.setName("Test Org");
        dto.setDescription("Test Description");
        dto.setEmail("test@example.com");
        dto.setIndustry("Test Industry");
        dto.setType("Test Type");
        dto.setCountry("Test Country");
        dto.setAddress("Test Address");
        dto.setState("Test State");

        Organisation org = new Organisation();
        org.setName(dto.getName());
        org.setDescription(dto.getDescription());
        org.setEmail(dto.getEmail());
        org.setIndustry(dto.getIndustry());
        org.setType(dto.getType());
        org.setCountry(dto.getCountry());
        org.setAddress(dto.getAddress());
        org.setState(dto.getState());
        org.setOwner(mockUser);

        Mockito.when(organisationServices.createOrganisation(Mockito.any(CreateOrganisationDTO.class), Mockito.any(User.class))).thenReturn(org);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Org\", \"description\": \"Test Description\", \"email\": \"test@example.com\", \"industry\": \"Test Industry\", \"type\": \"Test Type\", \"country\": \"Test Country\", \"address\": \"Test Address\", \"state\": \"Test State\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("organisation created successfully"))
                .andExpect(jsonPath("$.data.name").value("Test Org"));
    }

    @Test
    @WithMockUser
    void createOrganisation_ValidationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"description\": \"\", \"email\": \"invalid-email\", \"industry\": \"\", \"type\": \"\", \"country\": \"\", \"address\": \"\", \"state\": \"\"}")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void createOrganisation_Unauthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Org\", \"description\": \"Test Description\", \"email\": \"test@example.com\", \"industry\": \"Test Industry\", \"type\": \"Test Type\", \"country\": \"Test Country\", \"address\": \"Test Address\", \"state\": \"Test State\"}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("User not authenticated"));
    }


}
