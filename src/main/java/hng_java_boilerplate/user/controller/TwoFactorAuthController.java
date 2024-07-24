package hng_java_boilerplate.user.controller;

import hng_java_boilerplate.user.dto.request.EnableTwoFactorAuthRequest;
import hng_java_boilerplate.user.dto.response.ApiResponse;
import hng_java_boilerplate.user.service.TwoFactorAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
public class TwoFactorAuthController {

    @Autowired
    private TwoFactorAuthService twoFactorAuthService;

    @PostMapping("/enable")
    public ResponseEntity<?> enable2FA(@RequestBody EnableTwoFactorAuthRequest request) {
        try {
            var response = twoFactorAuthService.enableTwoFA(request);
            if (response.getMessage().equals("2FA setup initiated")) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error enabling 2FA");
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify2FA(@RequestBody Map<String, String> request) {
      var response = twoFactorAuthService.verifyTotpCode(request.get("totp_code"));
        try {
            if (response.getMessage().equals("2FA verified and enabled")) {
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }

            if (response.getMessage().equals("Invalid TOTP")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while verifying TOTP code");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disable2FA(@RequestBody Map<String, String> requestBody) {
        String password = requestBody.get("password");
        String totpCode = requestBody.get("totp_code");

        var response = twoFactorAuthService.disableTwoFA(password, totpCode);
        if (response.getStatus_code().equals("200")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/backup-codes")
    public ResponseEntity<?> generateBackupCodes(@RequestBody Map<String, String> requestBody) {
        String password = requestBody.get("password");
        String totpCode = requestBody.get("totp_code");

        var response = twoFactorAuthService.generateBackupCodes(password, totpCode);
        if (response.getStatus_code().equals("200")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @PostMapping("/recover")
    public ResponseEntity<?> recoverBackupCode(@RequestBody Map<String, String> requestBody) {
        String backupCode = requestBody.get("backup_code");

        var response = twoFactorAuthService.recoverBackupCode(backupCode);
        if (response.getStatus_code().equals("200")) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
