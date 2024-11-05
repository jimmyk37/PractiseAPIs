package sample.User;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;


import sample.base.HelperClass;

public class GetUserDetailByEmailTest extends HelperClass {

	private static final String GET_USER_DETAIL_ENDPOINT = "/api/getUserDetailByEmail";
	

//	@Test
//	public void testGetUserDetailByEmailSuccess() {
//		String email = "jimmy@example.com";
//		Map<String, String> params = new HashMap<>();
//		params.put("email", email);
//		
//		given()
//		.formParams(params)
//		.when()
//		.post()
//		.then()
//		.statusCode(200)
//		.time(lessThan(5000L))
//		.body("user.email", equalTo(email));
//	}

	@Test
	public void testNonexistentEmail() {
		String email = "nonexistent@example.com";
		Map<String, String> params = new HashMap<>();
		params.put("email", email);
		
		given()
		.formParams(params)
		.when()
		.post()
		.then()
		.statusCode(403)
		.time(lessThan(5000L));
	}

	@Test
	public void testMissingEmailParameter() {
		Map<String, String> params = new HashMap<>();
		params.put("email", "");
		
		given()
		.formParams(params)
		.when()
		.post()
		.then()
		.statusCode(403)
		.time(lessThan(5000L));
		
	}

	@Test
	public void testInvalidEmailFormat() {
		String invalidEmail = "invalidemail";
		Map<String, String> params = new HashMap<>();
		params.put("email", invalidEmail);
		given()
		.formParams(params)
		.when()
		.post()
		.then()
		.statusCode(403)
		.time(lessThan(5000L));
		
	}

}
