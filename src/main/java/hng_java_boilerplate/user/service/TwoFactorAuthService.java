package hng_java_boilerplate.user.service;

import com.google.zxing.WriterException;
import hng_java_boilerplate.user.dto.request.*;
import hng_java_boilerplate.user.dto.response.EnableTwoFactorAuthResponse;
import hng_java_boilerplate.user.dto.response.VerifyTwoFactorAuthResponse;

import java.io.IOException;

public interface TwoFactorAuthService {

    EnableTwoFactorAuthResponse enableTwoFA(EnableTwoFactorAuthRequest request) throws IOException, WriterException;

    VerifyTwoFactorAuthResponse verifyTotpCode(Verify2FARequest request);

    EnableTwoFactorAuthResponse disableTwoFA(Disable2FARequest request);

    EnableTwoFactorAuthResponse generateBackupCodes(BackupCodeRequest request);

    EnableTwoFactorAuthResponse recoverBackupCode(RecoverBackupCodeRequest request);

}
