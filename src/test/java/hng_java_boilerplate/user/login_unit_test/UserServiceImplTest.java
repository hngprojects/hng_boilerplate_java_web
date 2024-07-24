package hng_java_boilerplate.user.login_unit_test;

import hng_java_boilerplate.user.dto.request.LoginDto;
import hng_java_boilerplate.user.dto.response.LoginErrorResponse;
import hng_java_boilerplate.user.dto.response.LoginResponse;
import hng_java_boilerplate.user.entity.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
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
    void testLoginUser_Success() {
        LoginDto loginDto = new LoginDto("johndoe@example.com", "password1");
        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtils.createJwt.apply(user)).thenReturn("someToken");

        ResponseEntity<?> responseEntity = userService.loginUser(loginDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        LoginResponse response = (LoginResponse) responseEntity.getBody();
        assertNotNull(response);
        assertEquals("someToken", response.getToken());
    }

    @Test
    void testLoginUser_InvalidEmail() {
        LoginDto loginDto = new LoginDto("nonexistent@example.com", "password123");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = userService.loginUser(loginDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        LoginErrorResponse errorResponse = (LoginErrorResponse) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid request parameters", errorResponse.getError());
        assertEquals("Invalid email or password.", errorResponse.getMessage());
    }

    @Test
    void testLoginUser_InvalidPassword() {
        LoginDto loginDto = new LoginDto("john.doe@example.com", "wrongPassword");
        User user = new User();
        user.setEmail(loginDto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

        ResponseEntity<?> responseEntity = userService.loginUser(loginDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        LoginErrorResponse errorResponse = (LoginErrorResponse) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals("Authentication failed", errorResponse.getError());
        assertEquals("Invalid email or password.", errorResponse.getMessage());
    }
}
