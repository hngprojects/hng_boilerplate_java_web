package hng_java_boilerplate.user.verify_oto_test;

import hng_java_boilerplate.exception.BadRequestException;
import hng_java_boilerplate.user.dto.request.ForgotPasswordRequest;
import hng_java_boilerplate.user.dto.response.CustomResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.entity.VerificationToken;
import hng_java_boilerplate.user.repository.UserRepository;
import hng_java_boilerplate.user.repository.VerificationTokenRepository;
import hng_java_boilerplate.user.serviceImpl.EmailServiceImpl;
import hng_java_boilerplate.user.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVerifyOtpSuccess() {
        String email = "test@example.com";
        String token = "validToken";

        User user = new User();
        user.setEmail(email);
        user.setIsEnabled(false);

        VerificationToken verificationToken = new VerificationToken(user, token);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 10);
        verificationToken.setExpirationTime(cal.getTime());

        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseEntity<String> response = userServiceImpl.verifyOtp(email, token, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User verified successfully", response.getBody());
        assertTrue(user.getIsEnabled());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testVerifyOtpInvalidToken() {
        String email = "test@example.com";
        String token = "invalidToken";

        when(verificationTokenRepository.findByToken(token)).thenReturn(null);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.verifyOtp(email, token, request);
        });

        assertEquals("User not found with email: test@example.com", exception.getMessage());
    }

    @Test
    public void testVerifyOtpExpiredToken() {
        String email = "test@example.com";
        String token = "expiredToken";

        User user = new User();
        user.setEmail(email);
        user.setIsEnabled(false);

        VerificationToken verificationToken = new VerificationToken(user, token);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -10);
        verificationToken.setExpirationTime(cal.getTime());

        when(verificationTokenRepository.findByToken(token)).thenReturn(verificationToken);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.verifyOtp(email, token, request);
        });

        assertEquals("OTP has expired. A new OTP has been sent to your email.", exception.getMessage());
    }

    @Test
    public void testVerifyOtpUserNotFound() {
        String email = "notfound@example.com";
        String token = "validToken";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.verifyOtp(email, token, request);
        });

        assertEquals("User not found with email: notfound@example.com", exception.getMessage());
    }


    @Test
    public void testForgotPassword() {
        String email = "test@user.com";
        ForgotPasswordRequest passwordRequest = new ForgotPasswordRequest();
        passwordRequest.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        CustomResponse response = userServiceImpl.forgotPassword(passwordRequest, request);

        assertEquals(200, response.status_code());
        assertEquals("forgot password reset token generated successfully", response.message());

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
    }
}
