package hng_java_boilerplate.twofactor.service;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.twofactor.dtos.TwoFactorResponse;
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
        String secretKey = totpService.generateSecretKey();
        user.setSecretKey(secretKey);
        userService.save(user);
        String qrCode = totpService.getQRCode(secretKey);

        return ResponseEntity.status(200).body(new TwoFactorResponse(200, "2FA setup initiated", new HashMap<>() {{
            put("qrcode", qrCode);
        }}));
    }
}
