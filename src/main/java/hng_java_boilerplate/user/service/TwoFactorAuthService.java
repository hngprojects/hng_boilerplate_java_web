package hng_java_boilerplate.user.service;

import com.google.zxing.WriterException;
import hng_java_boilerplate.user.dto.request.EnableTwoFactorAuthRequest;
import hng_java_boilerplate.user.dto.response.EnableTwoFactorAuthResponse;
import hng_java_boilerplate.user.dto.response.VerifyTwoFactorAuthResponse;

import java.io.IOException;

public interface TwoFactorAuthService {

    EnableTwoFactorAuthResponse enableTwoFA(EnableTwoFactorAuthRequest request) throws IOException, WriterException;

    VerifyTwoFactorAuthResponse verifyTotpCode(String totpCode);

    EnableTwoFactorAuthResponse disableTwoFA(String password, String totpCode);

    EnableTwoFactorAuthResponse generateBackupCodes(String password, String totpCode);

    EnableTwoFactorAuthResponse recoverBackupCode(String backupCode);

}
