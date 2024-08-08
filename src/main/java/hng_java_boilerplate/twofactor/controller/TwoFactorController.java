package hng_java_boilerplate.twofactor.controller;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.twofactor.dtos.TwoFactorResponse;
import hng_java_boilerplate.twofactor.service.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/2fa")
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @PostMapping("enable")
    public ResponseEntity<TwoFactorResponse> enable2fa() throws QrGenerationException {
        return twoFactorService.enableTwoFactor();
    }
}
