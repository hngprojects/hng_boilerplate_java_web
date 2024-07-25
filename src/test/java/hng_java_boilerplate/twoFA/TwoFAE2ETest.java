package hng_java_boilerplate.twoFA;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TwoFAE2ETest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void whenEnable2FA_then2FASetupInitiated() {
        String requestBody = """
                {
                    "password": "AlexJohn123$"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/2fa/enable")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA setup initiated"));
    }

    @Test
    public void whenVerify2FA_then2FAVerifiedAndEnabled() {
        String requestBody = """
                {
                    "totp_code": "123456"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/2fa/verify")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA verified and enabled"));
    }

    @Test
    public void whenDisable2FA_then2FADisabled() {
        String requestBody = """
                {
                    "password": "user_password",
                    "totp_code": "123456"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/2fa/disable")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA has been disabled"));
    }

    @Test
    public void whenGenerateBackupCodes_thenBackupCodesGenerated() {
        String requestBody = """
                {
                    "password": "user_password",
                    "totp_code": "123456"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/2fa/backup-codes")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("New backup codes generated"));
    }

    @Test
    public void whenRecoverWithBackupCode_then2FAVerified() {
        String requestBody = """
                {
                    "backup_codes": "code1"
                }
                """;

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/2fa/recover")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA verified"));
    }

}
