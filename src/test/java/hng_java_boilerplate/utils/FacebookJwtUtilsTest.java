package hng_java_boilerplate.utils;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.ProfilePictureSource;
import hng_java_boilerplate.authentication.profile.entity.Profile;
import hng_java_boilerplate.authentication.profile.repository.ProfileRepository;
import hng_java_boilerplate.authentication.user.dto.request.OAuthDto;
import hng_java_boilerplate.authentication.user.dto.response.ApiResponse;
import hng_java_boilerplate.authentication.user.dto.response.OAuthResponse;
import hng_java_boilerplate.authentication.user.dto.response.ResponseData;
import hng_java_boilerplate.authentication.user.dto.response.UserResponse;
import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.enums.Role;
import hng_java_boilerplate.authentication.user.repository.UserRepository;
import hng_java_boilerplate.authentication.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.FacebookJwtUtils;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FacebookJwtUtilsTest {

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
    private FacebookJwtUtils facebookJwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.createJwt = mock(Function.class);
    }

    @Test
    void testFacebookOauthUserJWT_successfulLogin() {
        OAuthDto oAuthDto = new OAuthDto();
        oAuthDto.setIdToken("test-access-token");

        com.restfb.types.User facebookUser = new com.restfb.types.User();
        facebookUser.setId("123456789");
        facebookUser.setEmail("test@example.com");
        facebookUser.setFirstName("John");
        facebookUser.setLastName("Doe");

        ProfilePictureSource pictureSource = new ProfilePictureSource();
        pictureSource.setUrl("http://example.com/picture");
        facebookUser.setPicture(pictureSource);

        FacebookClient facebookClient = mock(DefaultFacebookClient.class);
        when(facebookClient.fetchObject("me", com.restfb.types.User.class,
                com.restfb.Parameter.with("fields", "id,email,first_name,last_name,picture"))).thenReturn(facebookUser);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(profileRepository.save(any(Profile.class))).thenReturn(new Profile());

        String userId = UUID.randomUUID().toString();
        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setEmail("test@example.com");
        savedUser.setUserRole(Role.ROLE_USER);
        savedUser.setCreatedAt(null);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

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
                .imr_url(authDto.getImg_url())
                .created_at(savedUser.getCreatedAt())
                .build();

        ResponseData data = new ResponseData("jwt-token", userResponse);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value(), "Login Successful!", data);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus_code());
        assertEquals("Login Successful!", response.getMessage());
        ResponseData responseData = (ResponseData) response.getData();
        assertEquals("jwt-token", responseData.getToken());
    }
}
