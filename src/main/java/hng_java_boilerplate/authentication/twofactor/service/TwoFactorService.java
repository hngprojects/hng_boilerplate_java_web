package hng_java_boilerplate.authentication.twofactor.service;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.authentication.twofactor.dtos.TotpRequest;
import hng_java_boilerplate.authentication.twofactor.dtos.TwoFactorResponse;
import hng_java_boilerplate.authentication.user.entity.User;
import hng_java_boilerplate.authentication.user.service.UserService;
import hng_java_boilerplate.exception.exception_class.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TwoFactorService {

    private final TotpService totpService;
    private final UserService userService;

    public ResponseEntity<TwoFactorResponse> enableTwoFactor() throws QrGenerationException {
        User user = userService.getLoggedInUser();
        if (user.getTwoFactorEnabled()) {
            return ResponseEntity.status(200).body(new TwoFactorResponse(200, "2FA already enabled", null));
        }
        user.setTwoFactorEnabled(true);
        String secretKey = totpService.generateSecretKey();
        user.setSecretKey(secretKey);
        userService.save(user);
        String qrCode = totpService.getQRCode(secretKey, user.getUsername());

        return ResponseEntity.status(200).body(new TwoFactorResponse(200, "2FA setup initiated", new HashMap<>() {{
            put("qrcode", qrCode);
        }}));
    }

    public ResponseEntity<TwoFactorResponse> verifyTotp(TotpRequest totp) {
        User user = userService.getLoggedInUser();
        String secretKey = user.getSecretKey();
        if (secretKey == null || !user.getTwoFactorEnabled() ) {
            throw new UnauthorizedException("Invalid request") {
            };
        }
        boolean valid = totpService.verifyTotp(totp.totp(), secretKey);
        if (!valid) {
            throw new UnauthorizedException("Invalid Totp code");
        }
        return ResponseEntity.ok(new TwoFactorResponse(200, "Totp code verified", null));
    }
}
