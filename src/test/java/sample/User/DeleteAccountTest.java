package sample.User;

import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.util.HashMap;
import java.util.Map;
import io.restassured.response.Response;
import sample.base.HelperClass;

public class DeleteAccountTest extends HelperClass {

	private static final String DELETE_ACCOUNT_ENDPOINT = "/api/deleteAccount";

	public void testDeleteAccountSuccess() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("email", "jimmy@example.com");
		formParams.put("password", "jimmy12345");
		
		Response response=given()
		.formParams(formParams)
		.when()
		.post()
		.then()
		.statusCode(200)
		.time(lessThan(5000L))
		.contentType("application/json")
		.extract().response();

		Assert.assertEquals(response.jsonPath().getInt("responseCode"), 200);
		Assert.assertEquals(response.jsonPath().getString("message"), "Account deleted!");
	}

	@Test
	public void testNonexistentEmail() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("email", "nonexistent@example.com");
		formParams.put("password", "password123");

		         given()
				.formParams(formParams)
				.when()
				.post()
				.then()
				.statusCode(403)
				.time(lessThan(5000L));
		         
	}

	@Test
	public void testMissingEmailParameter() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("password", "password123");

		         given()
				.formParams(formParams)
				.when()
				.post()
				.then()
				.statusCode(403)
				.time(lessThan(5000L));
		         }

	@Test
	public void testMissingPasswordParameter() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("email", "jimmy@example.com");

		         given()
				.formParams(formParams)
				.when()
				.post()
				.then()
				.statusCode(403)
				.time(lessThan(5000L));

			}

	@Test
	public void testInvalidEmailFormat() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("email", "invalidemail");
		formParams.put("password", "password123");

		         given()
				.formParams(formParams)
				.when()
				.post()
				.then()
				.statusCode(403)
				.time(lessThan(5000L));
		         
	}
	
	@Test
	public void testIncorrectPassword() {
		Map<String, String> formParams = new HashMap<>();
		formParams.put("email", "jimmy@example.com");
		formParams.put("password", "wrongpassword");

		given()
				.formParams(formParams)
				.when()
				.post()
				.then()
				.statusCode(403)
				.time(lessThan(5000L));
	}

}
