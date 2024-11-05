package sample.Product;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;
import sample.base.HelperClass;

public class ProductsListTest extends HelperClass {

	static Response response;

	@Test
	public void testGetAllProductsListSuccess() {

		given().when().get("/api/productsList").then().statusCode(200).time(lessThan(5000L));

	}

	public void testMinimumProducts() {

		         given()
				.when()
				.get("/api/productsList")
				.then()
				.body("products",notNullValue())
				.log()
				.ifValidationFails();
		
		
	}

	@Test
	public void testResponseTime() {
		given()
		.when()
		.get("/api/productsList")
		.then()
		.time(lessThan(5000L))
		.log()
		.ifValidationFails();
	}

	@Test
	public void testContentType() {
		given()
		.when()
		.get("/api/productsList")
		.then()
		.contentType("text/html; charset=utf-8");
	}

	@Test
	public void testResponseContainsProducts() {

		response = given()
				.when()
				.get("/api/productsList")
				.then()
				.extract()
				.response();

		Assert.assertNotNull(response.jsonPath().getList("products"), "Products list is null");

	}

	@Test
	public void testProductStructure() {
		given()
				.when()
				.get("/api/productsList")
				.then().statusCode(200)
				.time(lessThan(5000L))																// present
				.body("products[0].price", notNullValue()) // Check if the "price" key is present
				.body("products[0].brand", notNullValue()) // Check if the "brand" key is present
				.body("products[0].category", notNullValue()); // Check if the "category" key is present
	}

	@Test
	public void testSpecificProductDetails() {

		response = given()
				.when()
				.get("/api/productsList")
				.then()
				.statusCode(200) // Verify the status code is 200
				.extract()
				.response();

		Assert.assertEquals(response.jsonPath().getString("products.find{it.id==1}.name"), "Blue Top");
		Assert.assertEquals(response.jsonPath().getString("products.find{it.id==1}.price"), "Rs. 500");
		Assert.assertEquals(response.jsonPath().getString("products.find{it.id==1}.brand"), "Polo");
		Assert.assertEquals(response.jsonPath().getString("products.find{it.id==1}.category.category"), "Tops");

	}

	@Test
	public void testFilterByCategory() {
		String category = "Electronic";
		response = given()
				.when()
				.get("/api/productsList")
				.then()
				.statusCode(200)
				.extract()
				.response();

		Assert.assertTrue(response.jsonPath().getList("products.findAll { it.category.category == '" + category + "' }")
				.isEmpty(), "No products found for the category");
	}

	@Test(enabled = false)
	public void testInvalidEndpoint() {

		given()
		.when()
		.get("/api/productsList1111")
		.then()
		.statusCode(404);
	}

	@Test
	public void testUnsupportedMethod() {

		given()
		.when()
		.post("/api/productsList1111")
		.then()
		.statusCode(404);
	}
}
