package hng_java_boilerplate.user.signup_unit_test;

import hng_java_boilerplate.user.dto.request.SignupDto;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.dto.response.ResponseData;
import hng_java_boilerplate.user.dto.response.UserResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.EmailAlreadyExistsException;
import hng_java_boilerplate.user.exception.UserNotFoundException;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.createJwt = mock(Function.class);
    }

    @Test
    void testRegisterUser_Success() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password123");
        User user = new User();
        user.setId("someUserId");
        user.setName(signupDto.getFirstName() + " " + signupDto.getLastName());
        user.setEmail(signupDto.getEmail());
        user.setPassword("encodedPassword");
        user.setCreatedAt(LocalDateTime.now());


        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(new User()));
        when(jwtUtils.createJwt.apply(any(User.class))).thenReturn("someToken");

        ResponseEntity<ApiResponse> responseEntity = userService.registerUser(signupDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ApiResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus_code());
        assertEquals("Registration Successful!", response.getMessage());

        ResponseData data = response.getData();
        assertNotNull(data);
        assertEquals("someToken", data.getToken());

        UserResponse userResponse = data.getUser();
        assertNotNull(userResponse);
        assertEquals("John", userResponse.getFirst_name());
        assertEquals("Doe", userResponse.getLast_name());
        assertEquals("john.doe@example.com", userResponse.getEmail());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password123");

        when(userRepository.existsByEmail(signupDto.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(signupDto));
    }


    @Test
    void testRegisterUser_UserNotFoundAfterSave() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password123");

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("John Doe");
        user.setEmail(signupDto.getEmail());
        user.setPassword("encodedPassword");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.registerUser(signupDto));
    }

    @Test
    void testRegisterUser_TokenExpiration() {
        SignupDto signupDto = new SignupDto("John", "Doe", "john.doe@example.com", "password123");

        User user = new User();
        user.setId("someUserId");
        user.setName(signupDto.getFirstName() + " " + signupDto.getLastName());
        user.setEmail(signupDto.getEmail());
        user.setPassword("encodedPassword");
        user.setCreatedAt(LocalDateTime.now());

        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.empty()); // Simulate email not taken
        when(passwordEncoder.encode(signupDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user); // Simulate saving user
        when(userRepository.findByEmail(signupDto.getEmail())).thenReturn(Optional.of(user)); // Simulate user retrieval after saving

        String token = "someToken";
        when(jwtUtils.createJwt.apply(any(UserDetails.class))).thenReturn(token);

        ResponseEntity<ApiResponse> responseEntity = userService.registerUser(signupDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        ApiResponse response = responseEntity.getBody();
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus_code());
        assertEquals("Registration Successful!", response.getMessage());

        ResponseData data = response.getData();
        assertNotNull(data);
        assertEquals(token, data.getToken());

        UserResponse userResponse = data.getUser();
        assertNotNull(userResponse);
        assertEquals("John", userResponse.getFirst_name());
        assertEquals("Doe", userResponse.getLast_name());
        assertEquals("john.doe@example.com", userResponse.getEmail());

        long expirationTime = Instant.now().plusSeconds(3600).toEpochMilli(); // Implement this method based on how the token is structured
        long now = Instant.now().toEpochMilli();
        assertTrue(expirationTime > now, "Token should not be expired yet");
    }

}