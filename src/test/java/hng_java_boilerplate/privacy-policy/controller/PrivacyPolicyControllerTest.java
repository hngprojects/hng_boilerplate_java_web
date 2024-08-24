package hng_java_boilerplate.privacy_policy.controller;

import hng_java_boilerplate.privacy_policy.entity.PrivacyPolicy;
import hng_java_boilerplate.privacy_policy.service.PrivacyPolicyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PrivacyPolicyControllerTest {

    @Mock
    private PrivacyPolicyServiceImpl privacyPolicyService;

    @InjectMocks
    private PrivacyPolicyController privacyPolicyController;

    private MockMvc mockMvc;

    private PrivacyPolicy privacyPolicy;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(privacyPolicyController).build();
        id = UUID.randomUUID();
        privacyPolicy = new PrivacyPolicy();
        privacyPolicy.setId(id);
        privacyPolicy.setContent("Test Content");
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void createPrivacyPolicy_ShouldReturnCreatedPolicy() throws Exception {
        when(privacyPolicyService.createPrivacyPolicy(any(PrivacyPolicy.class))).thenReturn(privacyPolicy);

        mockMvc.perform(post("/api/v1/privacy-policy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Test Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Privacy policy created successfully."))
                .andExpect(jsonPath("$.data.content").value("Test Content"));

        verify(privacyPolicyService, times(1)).createPrivacyPolicy(any(PrivacyPolicy.class));
    }

    @Test
    void getAllPrivacyPolicies_ShouldReturnListOfPolicies() throws Exception {
        when(privacyPolicyService.getAllPrivacyPolicies()).thenReturn(List.of(privacyPolicy));

        mockMvc.perform(get("/api/v1/privacy-policy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Privacy policies retrieved successfully."))
                .andExpect(jsonPath("$.data[0].content").value("Test Content"));

        verify(privacyPolicyService, times(1)).getAllPrivacyPolicies();
    }

    @Test
    void getPrivacyPolicyById_ShouldReturnPolicy_WhenPolicyExists() throws Exception {
        when(privacyPolicyService.getPrivacyPolicyById(id)).thenReturn(privacyPolicy);

        mockMvc.perform(get("/api/v1/privacy-policy/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Privacy policy retrieved successfully."))
                .andExpect(jsonPath("$.data.content").value("Test Content"));

        verify(privacyPolicyService, times(1)).getPrivacyPolicyById(id);
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updatePrivacyPolicy_ShouldReturnUpdatedPolicy() throws Exception {
        when(privacyPolicyService.updatePrivacyPolicy(eq(id), any(PrivacyPolicy.class))).thenReturn(privacyPolicy);

        mockMvc.perform(put("/api/v1/privacy-policy/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"Updated Content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Privacy policy updated successfully."))
                .andExpect(jsonPath("$.data.content").value("Test Content"));

        verify(privacyPolicyService, times(1)).updatePrivacyPolicy(eq(id), any(PrivacyPolicy.class));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deletePrivacyPolicy_ShouldReturnSuccessMessage() throws Exception {
        doNothing().when(privacyPolicyService).deletePrivacyPolicy(id);

        mockMvc.perform(delete("/api/v1/privacy-policy/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Privacy policy deleted successfully."));

        verify(privacyPolicyService, times(1)).deletePrivacyPolicy(id);
    }
}
