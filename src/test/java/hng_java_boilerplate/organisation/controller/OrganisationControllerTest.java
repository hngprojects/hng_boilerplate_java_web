package hng_java_boilerplate.organisation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.organisation.dto.request.AddUserRequest;
import hng_java_boilerplate.organisation.dto.response.AddUserResponse;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(OrganisationController.class)
class OrganisationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrganisationService organisationService;
    @MockBean
    private ProductErrorHandler productErrorHandler;
    @MockBean
    private JwtUtils jwtUtils;

    private AddUserRequest addRequest;
    private AddUserResponse addResponse;

    @BeforeEach
    void setUp() {
        addRequest = new AddUserRequest();
        addRequest.setUser_id("user-id");

        addResponse = new AddUserResponse("org-id", "user-id");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddUserToOrganisation() throws Exception {
        when(organisationService.addUserToOrganisation(addRequest, "org-id")).thenReturn(addResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestString = objectMapper.writeValueAsString(addRequest);

        mockMvc.perform(post("/api/v1/organisations/org-id/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.organization_id").value(addResponse.organization_id()))
                .andExpect(jsonPath("$.user_id").value(addResponse.user_id()));

        verify(organisationService, times(1)).addUserToOrganisation(any(AddUserRequest.class), eq("org-id"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenWhenUserIsNotAdmin() throws Exception {
        mockMvc.perform(post("/api/v1/organisations/org-id/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user_id\":\"user-id\"}"))
                .andExpect(status().isForbidden());

        verify(organisationService, times(0)).addUserToOrganisation(any(AddUserRequest.class), eq("org-id"));
    }
}