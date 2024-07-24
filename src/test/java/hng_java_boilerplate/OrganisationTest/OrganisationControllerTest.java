package hng_java_boilerplate.OrganisationTest;

import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.service.OrganisationService;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(hng_java_boilerplate.organisation.controller.OrganisationController.class)
public class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganisationService organisationService;

    @MockBean
    private UserServiceImpl userService;

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteOrganisation_ValidRequest_NoContent() throws Exception {
        Organisation org = new Organisation();
        org.setId("org123");
        User user = new User();
        user.setEmail("user@example.com");


        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(organisationService.findOrganisationById("org123")).thenReturn(Optional.of(org));
        Mockito.doNothing().when(organisationService).softDeleteOrganisation("org123");

        mockMvc.perform(delete("/api/v1/organizations/org123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteOrganisation_InvalidOrgIdFormat_BadRequest() throws Exception {
        mockMvc.perform(delete("/api/v1/organizations/invalid-id-format"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to delete organization"))
                .andExpect(jsonPath("$.error").value("Invalid organization ID format"))
                .andExpect(jsonPath("$.status_code").value(400));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteOrganisation_NonExistentOrgId_NotFound() throws Exception {

        Mockito.when(organisationService.findOrganisationById("nonexistent-id")).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/organizations/nonexistent-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Failed to delete organization"))
                .andExpect(jsonPath("$.error").value("Invalid organization ID"))
                .andExpect(jsonPath("$.status_code").value(404));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteOrganisation_UserNotAuthorized_Unauthorized() throws Exception {
        Organisation org = new Organisation();
        org.setId("org123");
        User user = new User();
        user.setEmail("user@example.com");


        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(organisationService.findOrganisationById("org123")).thenReturn(Optional.of(org));

        mockMvc.perform(delete("/api/v1/organizations/org123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Failed to delete organization"))
                .andExpect(jsonPath("$.error").value("User not authorized to delete this organization"))
                .andExpect(jsonPath("$.status_code").value(401));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    public void deleteOrganisation_OwnerCheck_Failure() throws Exception {
        Organisation org = new Organisation();
        org.setId("org123");
        User user = new User();
        user.setEmail("user@example.com");


        Mockito.when(userService.getLoggedInUser()).thenReturn(user);
        Mockito.when(organisationService.findOrganisationById("org123")).thenReturn(Optional.of(org));

        mockMvc.perform(delete("/api/v1/organizations/org123"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Failed to delete organization"))
                .andExpect(jsonPath("$.error").value("User not authorized to delete this organization"))
                .andExpect(jsonPath("$.status_code").value(401));
    }
}
