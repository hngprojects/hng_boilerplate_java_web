package hng_java_boilerplate.utils;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.ProfilePictureSource;
import hng_java_boilerplate.profile.entity.Profile;
import hng_java_boilerplate.profile.repository.ProfileRepository;
import hng_java_boilerplate.user.dto.request.GoogleOAuthDto;
import hng_java_boilerplate.user.dto.response.*;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
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
        GoogleOAuthDto googleOAuthDto = new GoogleOAuthDto();
        googleOAuthDto.setIdToken("test-access-token");

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

        OAuthUserResponse userResponse = OAuthUserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .first_name(authDto.getFirst_name())
                .last_name(authDto.getLast_name())
                .fullname(authDto.getFirst_name() + " " + authDto.getLast_name())
                .role(savedUser.getUserRole().name())
                .access_token("jwt-token")
                .build();

        UserOAuthDetails userOAuthDetails = UserOAuthDetails.builder()
                .id(userResponse.getId())
                .email(userResponse.getEmail())
                .first_name(userResponse.getFirst_name())
                .last_name(userResponse.getLast_name())
                .fullname(userResponse.getFullname())
                .role(userResponse.getRole())
                .build();

        OAuthLastUserResponse oAuthLastUserResponse = OAuthLastUserResponse.builder()
                .user(userOAuthDetails)
                .build();

        OAuthBaseResponse response = new OAuthBaseResponse(HttpStatus.OK.value(), "User Created Successful!", userResponse.getAccess_token(), oAuthLastUserResponse);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus_code());
        assertEquals("User Created Successful!", response.getMessage());
        assertEquals("jwt-token", response.getAccess_token());
    }
}
