package hng_java_boilerplate.twoFA;

import com.google.zxing.WriterException;
import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.EnableTwoFactorAuthResponse;
import hng_java_boilerplate.user.dto.response.VerifyTwoFactorAuthResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidPasswordException;
import hng_java_boilerplate.user.exception.UnauthorizedException;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.user.serviceImpl.TwoFactorAuthServiceImpl;
import hng_java_boilerplate.util.TwoFactorAuthUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TwoFAAuthTest {

    @Mock
    private UserService userService;

    @Mock
    private TwoFactorAuthUtils twoFactorAuthUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private TwoFactorAuthServiceImpl twoFactorAuthService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("user@example.com");
        user.setPassword("encodedPassword"); // Assuming this is how it is stored in the database
        user.setIs2FAEnabled(false);
    }

    @Test
    public void testEnableTwoFA() throws IOException, WriterException {
        // Mock the methods used in the service
        when(userService.getLoggedInUser()).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(twoFactorAuthUtils.generateSecretKey()).thenReturn("secretKey");
        when(twoFactorAuthUtils.generateQRCode(anyString(), anyString())).thenReturn(new byte[]{});

        // Create the request object
        EnableTwoFactorAuthRequest request = new EnableTwoFactorAuthRequest();
        request.setPassword("password");

        // Call the service method
        EnableTwoFactorAuthResponse response = twoFactorAuthService.enableTwoFA(request);

        // Validate the response
        assertEquals("200", response.getStatus_code());
        assertEquals("2FA setup initiated", response.getMessage());
        assertTrue(user.isIs2FAEnabled());
        assertNotNull(user.getTwoFASecretKey());
        verify(userService, times(1)).saveUser(user);

        // Print the response for debugging purposes
        System.out.println("Response: " + response);
    }

    @Test
    public void testVerifyTotpCode() {
        user.setTwoFASecretKey("secretKey");
        when(userService.getLoggedInUser()).thenReturn(user);
        when(twoFactorAuthUtils.verifyTotpCode("secretKey", 123456)).thenReturn(true);
        when(twoFactorAuthUtils.generateBackupCodes()).thenReturn(new String[]{"code1", "code2"});

        Verify2FARequest request = new Verify2FARequest();
        request.setTotp_code("123456");

        VerifyTwoFactorAuthResponse response = twoFactorAuthService.verifyTotpCode(request);

        assertEquals("200", response.getStatus_code());
        assertEquals("2FA verified and enabled", response.getMessage());
        assertNotNull(user.getTwoFABackupCodes());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testDisableTwoFA() {
        user.setIs2FAEnabled(true);
        user.setTwoFASecretKey("secretKey");
        when(userService.getLoggedInUser()).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(twoFactorAuthUtils.verifyTotpCode("secretKey", 123456)).thenReturn(true);

        Disable2FARequest request = new Disable2FARequest();
        request.setPassword("password");
        request.setTotp_code("123456");

        EnableTwoFactorAuthResponse response = twoFactorAuthService.disableTwoFA(request);

        assertEquals("200", response.getStatus_code());
        assertEquals("2FA has been disabled", response.getMessage());
        assertFalse(user.isIs2FAEnabled());
        assertNull(user.getTwoFASecretKey());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testGenerateBackupCodes() {
        user.setTwoFASecretKey("secretKey");
        when(userService.getLoggedInUser()).thenReturn(user);
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(twoFactorAuthUtils.verifyTotpCode("secretKey", 123456)).thenReturn(true);
        when(twoFactorAuthUtils.generateBackupCodes()).thenReturn(new String[]{"code1", "code2"});

        BackupCodeRequest request = new BackupCodeRequest();
        request.setPassword("password");
        request.setTotp_code("123456");

        EnableTwoFactorAuthResponse response = twoFactorAuthService.generateBackupCodes(request);

        assertEquals("200", response.getStatus_code());
        assertEquals("New backup codes generated", response.getMessage());
        assertNotNull(user.getTwoFABackupCodes());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testEnableTwoFAWithInvalidPassword() {
        when(userService.getLoggedInUser()).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        EnableTwoFactorAuthRequest request = new EnableTwoFactorAuthRequest();
        request.setPassword("wrongPassword");

        assertThrows(InvalidPasswordException.class, () -> {
            twoFactorAuthService.enableTwoFA(request);
        });
    }

    @Test
    public void testEnableTwoFAWithUnauthenticatedUser() {
        when(userService.getLoggedInUser()).thenReturn(null);

        EnableTwoFactorAuthRequest request = new EnableTwoFactorAuthRequest();
        request.setPassword("password");

        assertThrows(UnauthorizedException.class, () -> {
            twoFactorAuthService.enableTwoFA(request);
        });
    }

}
