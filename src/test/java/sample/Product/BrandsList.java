package sample.Product;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.response.Response;
import sample.base.HelperClass;

public class BrandsList extends HelperClass {

	static Response response;

	@Test
	public void testGetAllBrandsListSuccess() {
		
		SoftAssert softAssert=new SoftAssert();

		response = given()
				.when()
				.get("/api/brandsList")
				.then()
				.log().ifError()
				.statusCode(200)
				.time(lessThan(5000L))
				.contentType("text/html; charset=utf-8")
				.extract().response();

	       	softAssert.assertNotNull(response.jsonPath().getList("brands"), "Brand list is null");

	        // Validate that the first brand contains the keys "id" and "brand"
	        softAssert.assertTrue(response.jsonPath().getMap("brands[0]").containsKey("id"), "Brand ID is missing");
	        softAssert.assertTrue(response.jsonPath().getMap("brands[0]").containsKey("brand"), "Brand name is missing");

	        // Verify the brand name for the brand with ID 3
	        String brandName = response.jsonPath().getString("brands.find { it.id == 3 }.brand");
	        softAssert.assertEquals(brandName, "Madame", "Brand name for ID 3 is incorrect");

	        // Assert all the soft assertions
	        softAssert.assertAll();

	}

	@Test
	public void testInvalidEndpoint() {

		given()
		.when()
		.get("/api/brandsList1111")
		.then()
		.statusCode(404);

	}

	@Test
	public void testUnsupportedMethod() {
		
		given()
		.when()
		.post("/api/brandsList1111")
		.then()
		.statusCode(404);

	}

}
