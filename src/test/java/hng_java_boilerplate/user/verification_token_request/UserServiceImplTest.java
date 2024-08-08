package hng_java_boilerplate.user.verification_token_request;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import hng_java_boilerplate.user.dto.request.EmailSenderDto;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.entity.VerificationToken;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.repository.VerificationTokenRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import hng_java_boilerplate.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private HttpServletRequest request;

    @Mock
    private User user;

    @Mock
    private EmailSenderDto emailSenderDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRequestToken() {
        when(emailSenderDto.getEmail()).thenReturn("test@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(emailService.generateToken()).thenReturn("dummy-token");
        userService.requestToken(emailSenderDto, request);

        verify(verificationTokenRepository).save(any(VerificationToken.class));
        verify(emailService).sendVerificationEmail(user, request, "dummy-token");
    }

    @Test
    public void testSaveVerificationTokenForUser() {
        // Arrange
        String token = "dummy-token";
        userService.saveVerificationTokenForUser(user, token);
        verify(verificationTokenRepository).save(argThat(verificationToken ->
                verificationToken.getUser().equals(user) &&
                        verificationToken.getToken().equals(token)
        ));
    }
}