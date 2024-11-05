package sample.Product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.select.Evaluator.IsEmpty;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

import sample.base.HelperClass;

public class SearchProductTest extends HelperClass{

	static Response response;
	private static final String SEARCH_PRODUCT_ENDPOINT = "/api/searchProduct";

	
	
	@Test
	public void testResponseTime() {
		
		given()
		.formParam("search_product", "tshirt")
		.when()
		.post(SEARCH_PRODUCT_ENDPOINT)
		.then()
		.statusCode(200)
		.time(lessThan(5000L));
		
		
//		
//		response = requestHandler.postFormRequest(SEARCH_PRODUCT_ENDPOINT, param);
//		
//	    Assert.assertTrue(response.getTime() < 5000L, "Response time is greater than 5 seconds");

	}
	@Test
	public void testSearchProductSuccess() {
		
		given()
		.formParam("search_product", "tshirt")
		.when()
		.post(SEARCH_PRODUCT_ENDPOINT)
		.then()
		.statusCode(200)
		.body("products", notNullValue());


	}
	
	@Test
	public void testContentType() {
		
		given()
		.formParam("search_product", "tshirt")
		.when()
		.post(SEARCH_PRODUCT_ENDPOINT)
		.then()
		.statusCode(200)
		.contentType("text/html; charset=utf-8");

	}
    

	
	@Test
	public void testProductStructure() {
		
		response = given()
				.formParam("search_product", "tshirt")
				.when()
				.post(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();
		
		softAssert.assertTrue(response.jsonPath().getMap("products[0]").containsKey("id"), "Product ID is missing");
		softAssert.assertTrue(response.jsonPath().getMap("products[0]").containsKey("name"), "Product name is missing");
		softAssert.assertTrue(response.jsonPath().getMap("products[0]").containsKey("price"), "Product price is missing");
		softAssert.assertTrue(response.jsonPath().getMap("products[0]").containsKey("brand"), "Product brand is missing");
		softAssert.assertTrue(response.jsonPath().getMap("products[0]").containsKey("category"), "Product category is missing");

	}

	@Test
	public void testSpecificProductDetails() {
		
		SoftAssert softAssert= new SoftAssert();
		response = given()
				.formParam("search_product", "tshirt")
				.when()
				.post(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();
		
		String productName = response.jsonPath().getString("products.find { it.id == 2 }.name");
		String productPrice = response.jsonPath().getString("products.find { it.id == 2 }.price");
		String productBrand = response.jsonPath().getString("products.find { it.id == 2 }.brand");
		String productCategory = response.jsonPath().getString("products.find { it.id == 2 }.category.category");

		softAssert.assertEquals(productName, "Men Tshirt");
		softAssert.assertEquals(productPrice, "Rs. 400");
		softAssert.assertEquals(productBrand, "H&M");
		softAssert.assertEquals(productCategory, "Tshirts");
		
		softAssert.assertAll();
	}
	
	@Test
	public void testInvalidProductName() {
		response = given()
				.formParam("search_product", "invalid pro")
				.when()
				.post(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();


		Assert.assertTrue(response.jsonPath().getList("products").isEmpty(), "Products list is not empty");

	}
	
	@Test
	public void testUnsupportedMethod() {
		
		response = given()
				.when()
				.get(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();
		         System.out.println(response.jsonPath().getInt("responseCode"));
		Assert.assertEquals(response.jsonPath().getInt("responseCode"), 405, "Expected status code is 405 Method Not Allowed");

	}
	
	@Test
	public void testMissingRequiredParameter() {
		
		response = given()
				.when()
				.post(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();
		
		Assert.assertEquals(response.jsonPath().getInt("responseCode"), 400, "Expected status code is 400 Bad Request");
	    

	}
	
	@Test
	public void testSpecialCharactersInProductName() {
		response = given()
				.formParam("search_product", "#$%^#^&%")
				.when()
				.post(SEARCH_PRODUCT_ENDPOINT)
				.then()
				.statusCode(200)
				.extract()
				.response();


		Assert.assertTrue(response.jsonPath().getList("products").isEmpty(), "Products list is not empty");

	}




	
}
