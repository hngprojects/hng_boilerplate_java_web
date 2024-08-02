package hng_java_boilerplate.OrganisationTest;

import hng_java_boilerplate.organisation.controller.OrganisationController;
import hng_java_boilerplate.organisation.dto.CreateOrganisationDTO;
import hng_java_boilerplate.organisation.dto.CreateRoleDTO;
import hng_java_boilerplate.organisation.dto.UpdateOrganisationDTO;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.entity.Role;
import hng_java_boilerplate.organisation.exception.ResourceNotFoundException;
import hng_java_boilerplate.organisation.exception.UnauthorizedException;
import hng_java_boilerplate.organisation.service.OrganisationServices;
import hng_java_boilerplate.product.errorhandler.ProductErrorHandler;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.util.JwtUtils;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrganisationController.class)
class OrganisationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganisationServices organisationServices;

    @MockBean
    private ProductErrorHandler productErrorHandler;

    @MockBean
    private JwtUtils jwtUtils;

    private User mockUser;
    private Organisation mockOrganisation;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setName("testUser");

        mockOrganisation = new Organisation();
        mockOrganisation.setId(UUID.randomUUID().toString());
        mockOrganisation.setName("Test Org");
        mockOrganisation.setDescription("Test Description");
        mockOrganisation.setEmail("test@example.com");
        mockOrganisation.setIndustry("Test Industry");
        mockOrganisation.setType("Test Type");
        mockOrganisation.setCountry("Test Country");
        mockOrganisation.setAddress("Test Address");
        mockOrganisation.setState("Test State");
        mockOrganisation.setOwner(mockUser);
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

        Mockito.when(organisationServices.createOrganisation(Mockito.any(CreateOrganisationDTO.class), Mockito.any(User.class))).thenReturn(mockOrganisation);

        mockMvc.perform(post("/api/v1/organisations")
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
        mockMvc.perform(post("/api/v1/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"description\": \"\", \"email\": \"invalid-email\", \"industry\": \"\", \"type\": \"\", \"country\": \"\", \"address\": \"\", \"state\": \"\"}")
                )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void createOrganisation_Unauthenticated() throws Exception {
        mockMvc.perform(post("/api/v1/organisations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Org\", \"description\": \"Test Description\", \"email\": \"test@example.com\", \"industry\": \"Test Industry\", \"type\": \"Test Type\", \"country\": \"Test Country\", \"address\": \"Test Address\", \"state\": \"Test State\"}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("User not authenticated"));
    }

    @Test
    @WithMockUser
    void updateOrganisation_Success() throws Exception {
        UpdateOrganisationDTO dto = new UpdateOrganisationDTO();
        dto.setName("Updated Org");
        dto.setDescription("Updated Description");

        Organisation updatedOrg = mockOrganisation;
        updatedOrg.setName(dto.getName());
        updatedOrg.setDescription(dto.getDescription());

        Mockito.when(organisationServices.updateOrganisation(Mockito.anyString(), Mockito.any(UpdateOrganisationDTO.class), Mockito.any(User.class)))
                .thenReturn(updatedOrg);

        mockMvc.perform(patch("/api/v1/organisations/" + mockOrganisation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Org\", \"description\": \"Updated Description\"}")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("organisation updated successfully"))
                .andExpect(jsonPath("$.data.name").value("Updated Org"));
    }

    @Test
    @WithMockUser
    void updateOrganisation_NotFound() throws Exception {
        Mockito.when(organisationServices.updateOrganisation(Mockito.anyString(), Mockito.any(UpdateOrganisationDTO.class), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Organisation not found"));

        mockMvc.perform(patch("/api/v1/organisations/" + mockOrganisation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Org\", \"description\": \"Updated Description\"}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void updateOrganisation_Unauthorized() throws Exception {
        Mockito.when(organisationServices.updateOrganisation(Mockito.anyString(), Mockito.any(UpdateOrganisationDTO.class), Mockito.any(User.class)))
                .thenThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(patch("/api/v1/organisations/" + mockOrganisation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Org\", \"description\": \"Updated Description\"}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void deleteOrganisation_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void deleteOrganisation_NotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Organisation not found"))
                .when(organisationServices).deleteOrganisation(Mockito.anyString(), Mockito.any(User.class));

        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void deleteOrganisation_Unauthorized() throws Exception {
        Mockito.doThrow(new UnauthorizedException("Unauthorized"))
                .when(organisationServices).deleteOrganisation(Mockito.anyString(), Mockito.any(User.class));

        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId())
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void getOrganisationById_Success() throws Exception {
        Mockito.when(organisationServices.getOrganisationById(Mockito.anyString())).thenReturn(mockOrganisation);

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Org"));
    }

    @Test
    @WithMockUser
    void getOrganisationById_NotFound() throws Exception {
        Mockito.when(organisationServices.getOrganisationById(Mockito.anyString())).thenThrow(new ResourceNotFoundException("Organisation not found"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void getUsersInOrganisation_Success() throws Exception {
        List<User> users = Collections.singletonList(mockUser);

        Mockito.when(organisationServices.getUsersInOrganisation(Mockito.anyString(), Mockito.any(User.class))).thenReturn(users);

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testUser"));
    }

    @Test
    @WithMockUser
    void getUsersInOrganisation_NotFound() throws Exception {
        Mockito.when(organisationServices.getUsersInOrganisation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Organisation not found"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/users")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void getUsersInOrganisation_Unauthorized() throws Exception {
        Mockito.when(organisationServices.getUsersInOrganisation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/users")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void removeUserFromOrganisation_Success() throws Exception {
        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId() + "/users/" + mockUser.getId())
                )
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void removeUserFromOrganisation_NotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found"))
                .when(organisationServices).removeUserFromOrganisation(Mockito.anyString(), Mockito.anyString(), Mockito.any(User.class));

        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId() + "/users/" + mockUser.getId())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    @WithMockUser
    void removeUserFromOrganisation_Unauthorized() throws Exception {
        Mockito.doThrow(new UnauthorizedException("Unauthorized"))
                .when(organisationServices).removeUserFromOrganisation(Mockito.anyString(), Mockito.anyString(), Mockito.any(User.class));

        mockMvc.perform(delete("/api/v1/organisations/" + mockOrganisation.getId() + "/users/" + mockUser.getId())
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void acceptInvitation_Success() throws Exception {
        Mockito.when(organisationServices.acceptInvitation(Mockito.anyString(), Mockito.any(User.class))).thenReturn(mockOrganisation);

        mockMvc.perform(post("/api/v1/organisations/accept-invite?token=sample-token")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Invitation accepted successfully"))
                .andExpect(jsonPath("$.data.name").value("Test Org"));
    }

    @Test
    @WithMockUser
    void acceptInvitation_NotFound() throws Exception {
        Mockito.when(organisationServices.acceptInvitation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Invitation not found"));

        mockMvc.perform(post("/api/v1/organisations/accept-invite?token=sample-token")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Invitation not found"));
    }

    @Test
    @WithMockUser
    void acceptInvitation_Unauthorized() throws Exception {
        Mockito.when(organisationServices.acceptInvitation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(post("/api/v1/organisations/accept-invite?token=sample-token")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void createRoleInOrganisation_Success() throws Exception {
        CreateRoleDTO dto = new CreateRoleDTO();
        dto.setName("Test Role");
        dto.setDescription("Test Description");

        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        Mockito.when(organisationServices.createRoleInOrganisation(Mockito.anyString(), Mockito.any(CreateRoleDTO.class), Mockito.any(User.class)))
                .thenReturn(role);

        mockMvc.perform(post("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Role\", \"description\": \"Test Description\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("role created successfully"))
                .andExpect(jsonPath("$.data.name").value("Test Role"));
    }

    @Test
    @WithMockUser
    void createRoleInOrganisation_NotFound() throws Exception {
        Mockito.when(organisationServices.createRoleInOrganisation(Mockito.anyString(), Mockito.any(CreateRoleDTO.class), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Organisation not found"));

        mockMvc.perform(post("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Role\", \"description\": \"Test Description\"}")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void createRoleInOrganisation_Unauthorized() throws Exception {
        Mockito.when(organisationServices.createRoleInOrganisation(Mockito.anyString(), Mockito.any(CreateRoleDTO.class), Mockito.any(User.class)))
                .thenThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(post("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Test Role\", \"description\": \"Test Description\"}")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void getAllRolesInOrganisation_Success() throws Exception {
        Role role = new Role();
        role.setName("Test Role");
        role.setDescription("Test Description");

        List<Role> roles = Collections.singletonList(role);

        Mockito.when(organisationServices.getAllRolesInOrganisation(Mockito.anyString(), Mockito.any(User.class)))
                .thenReturn(roles);

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("roles retrieved successfully"))
                .andExpect(jsonPath("$.data[0].name").value("Test Role"));
    }

    @Test
    @WithMockUser
    void getAllRolesInOrganisation_NotFound() throws Exception {
        Mockito.when(organisationServices.getAllRolesInOrganisation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Organisation not found"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Organisation not found"));
    }

    @Test
    @WithMockUser
    void getAllRolesInOrganisation_Unauthorized() throws Exception {
        Mockito.when(organisationServices.getAllRolesInOrganisation(Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new UnauthorizedException("Unauthorized"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/roles")
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Unauthorized"));
    }

    @Test
    @WithMockUser
    void getRoleDetails_Success() throws Exception {
        Role role = new Role();
        role.setName("Test Role");
        role.setDescription("Test Description");

        Mockito.when(organisationServices.getRoleDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(User.class)))
                .thenReturn(role);

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/roles/" + UUID.randomUUID())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("role details retrieved successfully"))
                .andExpect(jsonPath("$.data.name").value("Test Role"));
    }

    @Test
    @WithMockUser
    void getRoleDetails_NotFound() throws Exception {
        Mockito.when(organisationServices.getRoleDetails(Mockito.anyString(), Mockito.anyString(), Mockito.any(User.class)))
                .thenThrow(new ResourceNotFoundException("Role not found"));

        mockMvc.perform(get("/api/v1/organisations/" + mockOrganisation.getId() + "/roles/" + UUID.randomUUID())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Role not found"));
    }
}