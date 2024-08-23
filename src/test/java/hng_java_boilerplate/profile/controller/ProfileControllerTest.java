package hng_java_boilerplate.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.authentication.profile.controller.ProfileController;
import hng_java_boilerplate.authentication.profile.dto.request.DeactivateUserRequest;
import hng_java_boilerplate.authentication.profile.dto.response.DeactivateUserResponse;
import hng_java_boilerplate.authentication.profile.service.ProfileService;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProfileController.class)
@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProfileService profileService;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void shouldCreateMockMvC() {
        assertThat(mockMvc).isNotNull();
    }

    @Test
    void shouldDeactivateUser() throws Exception {
        // request
        DeactivateUserRequest request = DeactivateUserRequest
                .builder()
                .confirmation("TRUE")
                .build();

        // mock response
        DeactivateUserResponse response =
                new DeactivateUserResponse(200, "user deactivated successfully");

        // mock service
        when(profileService.deactivateUser(request)).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestString = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/accounts/deactivate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status_code").value(200))
                .andExpect(jsonPath("$.message").value(response.message()));
    }
}