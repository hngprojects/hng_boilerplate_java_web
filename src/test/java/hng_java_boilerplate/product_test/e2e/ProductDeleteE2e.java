package hng_java_boilerplate.product_test.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProductDeleteE2e {
    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://localhost:8080";
    }
    @Test
    public void whenDeleteProductWithValidId_thenSuccess(){
        String productId = "validProductId";
        given().contentType("application/json").when().delete("/api/v1/products", productId).then().statusCode(204);
    }

    @Test
    public void whenDeleteProductWithoutPermission_thenForbidden(){
        String productId = "validProductIdWithoutPermission";
        given().contentType("application/json").when().delete("/api/v1/products", productId).then().statusCode(403).body("message",equalTo("You do not have permission to delete this product"))
    }
    @Test
    public void whenDeleteNonExistentProduct_thenReturnNotFound(){
        String invalidProductId = "invalidProductId";
        given().contentType("application/json").when().delete("/api/v1/products"+ invalidProductId).then().statusCode(404).body("status_code",equalTo(404)).body("message",equalTo("Product not found"));
    }

    @Test
    public void whenDeleteProduct_InternalServerError(){
        String invalidProductId = "validProductId";
        given().contentType("application/json").when().delete("/api/v1/products"+ invalidProductId).then().statusCode(404).body("status_code",equalTo(404)).body("message",equalTo("Product not found"));
    }
}

