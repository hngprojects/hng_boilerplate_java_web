package hng_java_boilerplate.user.serviceImpl;

import com.google.zxing.ReaderException;
import com.google.zxing.WriterException;
import hng_java_boilerplate.user.dto.request.EnableTwoFactorAuthRequest;
import hng_java_boilerplate.user.dto.response.EnableTwoFactorAuthResponse;
import hng_java_boilerplate.user.dto.response.VerifyTwoFactorAuthResponse;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.exception.InvalidPasswordException;
import hng_java_boilerplate.user.exception.UnauthorizedException;
import hng_java_boilerplate.user.service.TwoFactorAuthService;
import hng_java_boilerplate.user.service.UserService;
import hng_java_boilerplate.util.TwoFactorAuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TwoFactorAuthServiceImpl implements TwoFactorAuthService {

    private final UserService userService;

    private final TwoFactorAuthUtils twoFactorAuthUtils;

    private final PasswordEncoder passwordEncoder;

    public TwoFactorAuthServiceImpl(UserService userService, TwoFactorAuthUtils twoFactorAuthUtils, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.twoFactorAuthUtils = twoFactorAuthUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public EnableTwoFactorAuthResponse enableTwoFA(EnableTwoFactorAuthRequest request) throws IOException, WriterException {
        EnableTwoFactorAuthResponse response = new EnableTwoFactorAuthResponse();
        Map<String, Object> data = new HashMap<>();
        String password = request.getPassword();
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            if (loggedInUser.getPassword().equals(password)) {
                if (!loggedInUser.isIs2FAEnabled()) {
                    loggedInUser.setIs2FAEnabled(true);
                    String secretKey = twoFactorAuthUtils.generateSecretKey();
                    loggedInUser.setTwoFASecretKey(secretKey);
                    userService.saveUser(loggedInUser);
                    byte[] qrCode = twoFactorAuthUtils.generateQRCode(secretKey);
                    response.setStatus_code("200");
                    response.setMessage("2FA setup initiated");
                    data.put("secret_key", secretKey);
                    data.put("qr_code_url", qrCode);
                    response.setData(data);
                    return response;
                }
            } else {
                throw new InvalidPasswordException("Password do not match");
            }
        } else {
            throw new UnauthorizedException("Authentication required");
        }
        response.setStatus_code("401");
        response.setMessage("Authentication required");
        return response;
    }

    @Override
    public VerifyTwoFactorAuthResponse verifyTotpCode(String totpCode) {
        VerifyTwoFactorAuthResponse response = new VerifyTwoFactorAuthResponse();
        Map<String, Object> data = new HashMap<>();
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            String secretKey = loggedInUser.getTwoFASecretKey();
            int code = Integer.parseInt(totpCode);
            boolean isVerified = twoFactorAuthUtils.verifyTotpCode(secretKey, code);
            if (isVerified) {
                String[] backupCodes = twoFactorAuthUtils.generateBackupCodes();
                loggedInUser.setTwoFABackupCodes(List.of(backupCodes));
                userService.saveUser(loggedInUser);
                response.setStatus_code("200");
                response.setMessage("2FA verified and enabled");
                data.put("backup_codes", backupCodes);
                response.setData(data);
                return response;
            } else {
                response.setStatus_code("400");
                response.setMessage("Invalid TOTP");
                return response;
            }
        } else {
            throw new UnauthorizedException("Authentication required");
        }

    }

    @Override
    public EnableTwoFactorAuthResponse disableTwoFA(String password, String totpCode) {
        EnableTwoFactorAuthResponse response = new EnableTwoFactorAuthResponse();
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            if (passwordEncoder.matches(password, loggedInUser.getPassword())) {
                int code = Integer.parseInt(totpCode);
                boolean isVerified = twoFactorAuthUtils.verifyTotpCode(loggedInUser.getTwoFASecretKey(), code);
                if (isVerified) {
                    loggedInUser.setIs2FAEnabled(false);
                    loggedInUser.setTwoFASecretKey(null);
                    userService.saveUser(loggedInUser);
                    response.setStatus_code("200");
                    response.setMessage("2FA has been disabled");
                    return response;
                } else {
                    response.setStatus_code("400");
                    response.setMessage("Invalid TOTP code");
                    return response;
                }
            } else {
                response.setStatus_code("401");
                response.setMessage("Invalid password");
                return response;
            }
        } else {
            response.setStatus_code("401");
            response.setMessage("Authentication required");
            return response;
        }
    }

    @Override
    public EnableTwoFactorAuthResponse generateBackupCodes(String password, String totpCode) {
        EnableTwoFactorAuthResponse response = new EnableTwoFactorAuthResponse();
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            if (passwordEncoder.matches(password, loggedInUser.getPassword())) {
                int code = Integer.parseInt(totpCode);
                boolean isVerified = twoFactorAuthUtils.verifyTotpCode(loggedInUser.getTwoFASecretKey(), code);
                if (isVerified) {
                    String[] backupCodes = twoFactorAuthUtils.generateBackupCodes();
                    loggedInUser.setTwoFABackupCodes(List.of(backupCodes));
                    userService.saveUser(loggedInUser);
                    response.setStatus_code("200");
                    response.setMessage("New backup codes generated");
                    response.setData(Map.of("backup_codes", backupCodes));
                    return response;
                } else {
                    response.setStatus_code("400");
                    response.setMessage("Invalid TOTP code");
                    return response;
                }
            } else {
                response.setStatus_code("401");
                response.setMessage("Invalid password");
                return response;
            }
        } else {
            response.setStatus_code("401");
            response.setMessage("Authentication required");
            return response;
        }
    }

    @Override
    public EnableTwoFactorAuthResponse recoverBackupCode(String backupCode) {
        EnableTwoFactorAuthResponse response = new EnableTwoFactorAuthResponse();
        User loggedInUser = userService.getLoggedInUser();
        if (loggedInUser != null) {
            if (loggedInUser.getTwoFABackupCodes() != null && List.of(loggedInUser.getTwoFABackupCodes()).contains(backupCode)) {
                // Logic to verify and recover 2FA
                response.setStatus_code("200");
                response.setMessage("2FA verified");
                return response;
            } else {
                response.setStatus_code("400");
                response.setMessage("Invalid backup code");
                return response;
            }
        } else {
            response.setStatus_code("401");
            response.setMessage("Authentication required");
            return response;
        }
    }

}
