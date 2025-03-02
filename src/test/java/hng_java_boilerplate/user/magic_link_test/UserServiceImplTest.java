package hng_java_boilerplate.user.magic_link_test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import hng_java_boilerplate.exception.UnAuthorizedException;
import hng_java_boilerplate.organisation.entity.Organisation;
import hng_java_boilerplate.organisation.repository.OrganisationRepository;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.entity.MagicLinkToken;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.repository.MagicLinkTokenRepository;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrganisationRepository organisationRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private MagicLinkTokenRepository magicLinkTokenRepository;

    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    public void testSendMagicLink_UserExists() {
        String email = "test@example.com";
        User existingUser = new User();
        existingUser.setEmail(email);
        when(userRepository.existsByEmail(email)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        userService.sendMagicLink(email, request);
        verify(magicLinkTokenRepository, times(1)).save(any(MagicLinkToken.class));
        verify(emailService, times(1)).sendMagicLink(eq(email), eq(request), anyString());
    }

    @Test
    public void testSendMagicLink_UserDoesNotExist() {
        String email = "newuser@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(organisationRepository.save(any())).thenReturn(new Organisation());
        userService.sendMagicLink(email, request);
        verify(magicLinkTokenRepository, times(1)).save(any(MagicLinkToken.class));
        verify(emailService, times(1)).sendMagicLink(eq(email), eq(request), anyString());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testMagicLinkLogin_ExpiredToken() {
        String token = "expiredToken";
        User user = new User();
        user.setEmail("test@example.com");
        MagicLinkToken magicLinkToken = new MagicLinkToken(user, token);
        magicLinkToken.setExpirationTime(new Date(System.currentTimeMillis() - 1000)); // Set to past
        when(magicLinkTokenRepository.findByToken(token)).thenReturn(magicLinkToken);
        ResponseEntity<?> response = userService.magicLinkLogin(token);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Your token has expired"));
        verify(magicLinkTokenRepository, times(1)).save(any(MagicLinkToken.class));
    }

    @Test
    public void testMagicLinkLogin_InvalidToken() {
        String token = "invalidToken";
        when(magicLinkTokenRepository.findByToken(token)).thenReturn(null);
        Exception exception = assertThrows(UnAuthorizedException.class, () -> {
            userService.magicLinkLogin(token);
        });
        assertEquals("Unknown request", exception.getMessage());
    }
}