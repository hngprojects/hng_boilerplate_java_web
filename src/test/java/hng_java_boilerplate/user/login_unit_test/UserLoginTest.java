package hng_java_boilerplate.user.login_unit_test;

import hng_java_boilerplate.activitylog.service.ActivityLogService;
import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.enums.Role;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserLoginTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ActivityLogService activityLogService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.createJwt = mock(Function.class);  // Mock the createJwt function
    }

    @Test
    public void testLoginUser_Success() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("testuser@example.com");
        loginDto.setPassword("ValidPassword1");

        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setName("John Doe");
        mockUser.setEmail("testuser@example.com");
        mockUser.setPassword(passwordEncoder.encode("ValidPassword1"));  // Mock encoded password
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUserRole(Role.ROLE_USER);

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));  // Mock findById
        when(passwordEncoder.matches(loginDto.getPassword(), mockUser.getPassword())).thenReturn(true);
        when(jwtUtils.createJwt.apply(any(UserDetails.class))).thenReturn("mockedToken");

        ResponseEntity<ApiResponse> responseEntity = userService.loginUser(loginDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ApiResponse apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Login Successful!", apiResponse.getMessage());

        ResponseData data = (ResponseData) apiResponse.getData();
        assertNotNull(data);
        assertEquals("mockedToken", data.getToken());

        UserResponse userResponse = data.getUser();
        assertNotNull(userResponse);
        assertEquals(mockUser.getId(), userResponse.getId());
        assertEquals("John", userResponse.getFirst_name());
        assertEquals("Doe", userResponse.getLast_name());
        assertEquals(mockUser.getEmail(), userResponse.getEmail());
        assertEquals(mockUser.getCreatedAt(), userResponse.getCreated_at());
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        String email = "test@example.com";
        String password = "wrongPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("correctPassword"));  // Mock encoded password
        user.setId("1");

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

        ResponseEntity<ApiResponse> responseEntity = userService.loginUser(loginDto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        ApiResponse apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("Invalid email or password", apiResponse.getMessage());
    }

    @Test
    void testLoginUser_Failure_UserNotFound() {
        String email = "notfound@example.com";
        String password = "password";

        LoginDto loginRequest = new LoginDto(email, password);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(
                BadRequestException.class,
                () -> userService.loadUserByUsername(email)
        );
    }

    @Test
    void testCreateJwtToken() {
        User user = new User();
        user.setId("someUserId");

        when(jwtUtils.createJwt.apply(any(UserDetails.class))).thenReturn("generatedToken");
        String token = jwtUtils.createJwt.apply(user);
        assertEquals("generatedToken", token);
    }
}
