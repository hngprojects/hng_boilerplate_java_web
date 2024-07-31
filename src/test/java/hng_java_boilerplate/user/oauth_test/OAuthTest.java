package hng_java_boilerplate.user.oauth_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import hng_java_boilerplate.user.controller.AuthController;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.util.GoogleJwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OAuthTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private GoogleJwtUtils googleJwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testAuthorizeOauthUser() throws Exception {
        String simulatedToken = "mockedToken";

        LocalDateTime now = LocalDateTime.now();
        UserResponse userResponse = new UserResponse(
                "1",
                "John",
                "Doe",
                "john.doe@example.com",
                "USER_ROLE",
                "mockedImageUrl",
                now
        );

        ResponseData responseData = new ResponseData("mockedJwtToken", userResponse);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK.value(), "Login Successful!", responseData);
        ResponseEntity<ApiResponse> responseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        when(googleJwtUtils.googleOauthUserJWT(anyString())).thenReturn(responseEntity);

        mockMvc.perform(post("/api/v1/auth/google")
                        .param("token", simulatedToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Login Successful!"))
                .andExpect(jsonPath("$.data.token").value("mockedJwtToken"))
                .andExpect(jsonPath("$.data.user.id").value("1"))
                .andExpect(jsonPath("$.data.user.first_name").value("John"))
                .andExpect(jsonPath("$.data.user.last_name").value("Doe"))
                .andExpect(jsonPath("$.data.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.data.user.role").value("USER_ROLE"))
                .andExpect(jsonPath("$.data.user.imr_url").value("mockedImageUrl"));
    }
}
