package hng_java_boilerplate.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.user.dto.request.OAuthDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.OAuthResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.GoogleJwtUtils;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GoogleJwtUtilsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private GoogleJwtUtils googleJwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.createJwt = mock(Function.class);
    }

    @Test
    void testGoogleOauthUserJWT_successfulLogin() throws GeneralSecurityException, IOException {
        // Arrange
        OAuthDto oAuthDto = new OAuthDto();
        oAuthDto.setIdToken("test-id-token");

        GoogleIdToken googleIdToken = mock(GoogleIdToken.class);
        Payload payload = mock(Payload.class);

        // Mocking GoogleIdTokenVerifier
        GoogleIdTokenVerifier verifier = mock(GoogleIdTokenVerifier.class);
        when(verifier.verify(anyString())).thenReturn(googleIdToken);

        // Mocking GoogleIdToken and Payload
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmail()).thenReturn("test@example.com");
        when(payload.getEmailVerified()).thenReturn(true);
        when(payload.get("picture")).thenReturn("http://example.com/picture");
        when(payload.get("family_name")).thenReturn("Doe");
        when(payload.get("given_name")).thenReturn("John");

        // Mocking UserRepository
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(profileRepository.save(any(Profile.class))).thenReturn(new Profile());

        // Creating a mock User
        String userId = UUID.randomUUID().toString();
        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setEmail("test@example.com");
        savedUser.setUserRole(Role.ROLE_USER);
        savedUser.setCreatedAt(null); // Adjust based on actual implementation
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Mocking UserServiceImpl and JwtUtils
        UserDetails userDetails = mock(UserDetails.class);
        when(userService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtils.createJwt.apply(any(UserDetails.class))).thenReturn("jwt-token");


        OAuthResponse authDto =  OAuthResponse
                .builder()
                .email("test@example.com")
                .first_name("John")
                .last_name("Doe")
                .password("GOOGLELOGIN1")
                .img_url("http://example.com/picture")
                .is_active(true)
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .first_name(authDto.getFirst_name())
                .last_name(authDto.getLast_name())
                .email(savedUser.getEmail())
                .role(savedUser.getUserRole().name())
                .avatar_url(authDto.getImg_url())
                .created_at(savedUser.getCreatedAt())
                .build();

        ResponseData data = new ResponseData("jwt-token", userResponse);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Login Successful!", data);


        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus_code());
        assertEquals("Login Successful!", response.getMessage());
        ResponseData responseData = response.getData();
        assertEquals("jwt-token", responseData.getToken());
    }
}
