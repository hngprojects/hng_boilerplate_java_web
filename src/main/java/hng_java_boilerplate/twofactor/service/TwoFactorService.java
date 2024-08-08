package hng_java_boilerplate.twofactor.service;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.twofactor.dtos.TotpRequest;
import hng_java_boilerplate.twofactor.dtos.TwoFactorResponse;
import hng_java_boilerplate.twofactor.exception.InvalidTotpException;
import hng_java_boilerplate.user.entity.User;
import hng_java_boilerplate.user.service.UserService;
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
        boolean valid = totpService.verifyTotp(totp.totp(), user.getSecretKey());
        if (!valid) {
            throw new InvalidTotpException("Invalid Totp code");
        }
        return ResponseEntity.ok(new TwoFactorResponse(200, "Totp code verified", null));
    }
}
