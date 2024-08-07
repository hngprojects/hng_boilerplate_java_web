package hng_java_boilerplate.user.passwordReset;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ForgetPasswordE2ETest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }


    public void test() {
        String requestBody = "{\"email\": \"test@example.com\"}";
        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/users/forgot-password")
                .then()
                .statusCode(200)
                .body("status", equalTo("200"))
                .body("message", equalTo("Password reset email sent successfully"));
    }

}
