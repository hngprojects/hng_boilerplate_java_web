package hng_java_boilerplate.twoFA;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TwoFAE2ETest {

    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";

        Response response = given()
                .contentType("application/json")
                .body("{ \"username\": \"testuser\", \"password\": \"testpassword\" }")
                .when()
                .post("/api/v1/auth/register")
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = response.jsonPath().getString("token");
    }

    @Test
    public void whenEnable2FA_thenReturnSuccess() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{ \"password\": \"testpassword\" }")
                .when()
                .post("/api/v1/2fa/enable")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA setup initiated"));
    }

    @Test
    public void whenVerify2FA_thenReturnSuccess() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{ \"totp_code\": \"123456\" }")
                .when()
                .post("/api/v1/2fa/verify")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA verified and enabled"));
    }

    @Test
    public void whenDisable2FA_thenReturnSuccess() {
        given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + token)
                .body("{ \"password\": \"testpassword\", \"totp_code\": \"123456\" }")
                .when()
                .post("/api/v1/2fa/disable")
                .then()
                .statusCode(200)
                .body("status_code", equalTo("200"))
                .body("message", equalTo("2FA has been disabled"));
    }

}
