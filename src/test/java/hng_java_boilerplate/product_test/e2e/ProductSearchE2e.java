package hng_java_boilerplate.product_test.e2e;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductSearchE2e {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void whenSearchByName_thenReturnProducts() {
        given()
                .contentType("application/json")
                .param("name", "Smartphone x")
                .param("category", "Electronics")
                .param("minPrice", "600.00")
                .param("maxPrice", "800.00")
                .when()
                .get("/api/v1/products/search")
                .then()
                .statusCode(200)
                .body("status_code", equalTo(200))
                .body("products[0].name", equalTo("Smartphone X"));
    }

    @Test
    public void whenSearchWithNoResults_thenReturnEmptyContent() {
        given()
                .contentType("application/json")
                .param("name", "NonExistentProduct")
                .when()
                .get("/api/v1/products/search")
                .then()
                .statusCode(200)
                .body("status_code", equalTo(204))
                .body("products.size()", equalTo(0));
    }

    @Test
    public void whenSearchWithInvalidParameterFormat_thenReturnUnprocessableEntity() {
        given()
                .contentType("application/json")
                .param("name", "")
                .param("minPrice", "10.0")
                .param("maxPrice", "20.0")
                .when()
                .get("/api/v1/products/search")
                .then()
                .statusCode(422)
                .body("status_code", equalTo(422));
    }
}
