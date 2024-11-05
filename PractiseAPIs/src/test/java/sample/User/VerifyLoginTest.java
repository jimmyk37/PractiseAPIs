package sample.User;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import sample.base.HelperClass;

public class VerifyLoginTest extends HelperClass {
	
	
	
	private static final String VERIFY_LOGIN_ENDPOINT = "/api/verifyLogin";

    @Test
    public void testVerifyLoginSuccess() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("email", "jimmy@example.com");
        formParams.put("password", "jimmy12345");
        
        Response response = given()
        .formParams(formParams)
        .when()
        .post(VERIFY_LOGIN_ENDPOINT)
        .then()
        .statusCode(200)
        .contentType("text/html; charset=utf-8")
        .time(lessThan(5000L))
        .extract()
        .response(); 
        
        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "User exists!");
    }

    @Test
    public void testIncorrectPassword() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("email", "johndoe@example.com");
        formParams.put("password", "incorrectPassword");

        Response response = given()
                .formParams(formParams)
                .when()
                .post(VERIFY_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType("text/html; charset=utf-8")
                .time(lessThan(5000L))
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 404, "Expected status code is 404 Unauthorized");
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found!", "Expected msg is User not found!");
    }

    @Test
    public void testNonexistentUser() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("email", "johndoe@example1.com");
        formParams.put("password", "incorrectPassword");

        Response response = given()
                .formParams(formParams)
                .when()
                .post(VERIFY_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType("text/html; charset=utf-8")
                .time(lessThan(5000L))
                .extract()
                .response();

        
        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 404, "Expected status code is 404 Not Found");
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found!", "Expected msg is User not found!");
    }

    @Test
    public void testMissingEmail() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("email", "johndoe@example1.com");
        formParams.put("password", "");

        Response response = given()
                .formParams(formParams)
                .when()
                .post(VERIFY_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType("text/html; charset=utf-8")
                .time(lessThan(5000L))
                .extract()
                .response();
        
        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 404, "Expected status code is 404 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found!", "Expected msg is User not found!");
    }

    @Test
    public void testEmptyRequest() {
        Map<String, String> formParams = new HashMap<>();
        formParams.put("email", "");
        formParams.put("password", "");

        Response response = given()
                .formParams(formParams)
                .when()
                .post(VERIFY_LOGIN_ENDPOINT)
                .then()
                .statusCode(200)
                .contentType("text/html; charset=utf-8")
                .time(lessThan(5000L))
                .extract()
                .response();
        Assert.assertEquals(response.jsonPath().getInt("responseCode"), 404, "Expected status code is 404 Bad Request");
        Assert.assertEquals(response.jsonPath().getString("message"), "User not found!", "Expected msg is User not found!");
    }
}
