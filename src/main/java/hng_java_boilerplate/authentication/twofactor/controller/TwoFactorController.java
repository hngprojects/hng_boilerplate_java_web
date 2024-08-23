package hng_java_boilerplate.authentication.twofactor.controller;

import dev.samstevens.totp.exceptions.QrGenerationException;
import hng_java_boilerplate.authentication.twofactor.dtos.EnableTwoFactorRequest;
import hng_java_boilerplate.authentication.twofactor.dtos.TotpRequest;
import hng_java_boilerplate.authentication.twofactor.dtos.TwoFactorResponse;
import hng_java_boilerplate.authentication.twofactor.service.TwoFactorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/auth/2fa")
public class TwoFactorController {

    private final TwoFactorService twoFactorService;

    @PostMapping("enable")
    public ResponseEntity<TwoFactorResponse> enable2fa(@RequestBody EnableTwoFactorRequest request) throws QrGenerationException {
        return twoFactorService.enableTwoFactor();
    }

    @PostMapping("verify")
    public ResponseEntity<TwoFactorResponse> verify(@RequestBody TotpRequest totp) {
        return twoFactorService.verifyTotp(totp);
    }
}
