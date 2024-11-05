package sample.base;

import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RequestHandler {
    

    private RequestSpecification request;

    public RequestHandler() {      
        this.request = RestAssured.given();
    }

 // Method for setting API Key
    public void setApiKey(String apiKey) {
    	request.header("x-api-key", apiKey);
    }

    // Method for setting Basic Authentication
    public void setBasicAuth(String username, String password) {
    	request.auth().preemptive().basic(username, password);
    }

    // Method for setting Bearer Token
    public void setBearerToken(String bearerToken) {
    	request.header("Authorization", "Bearer " + bearerToken);
    }

    // Method for setting HMAC Authentication
    public void setHmacAuth(String secretKey, String message) {
        String hmacSignature = generateHmac(secretKey, message);
        request.header("Authorization", "HMAC " + hmacSignature);
    }

    // Method for GET request
    public Response get(String endpoint) {
        return request.when().get(endpoint);
    }

    // Method for generating HMAC
    private String generateHmac(String secret, String data) {
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKeySpec);
            byte[] hmacBytes = sha256HMAC.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC", e);
        }
    }
       // Method to make a GET request
    public Response getRequest(String endpoint) {
        RequestSpecification request = RestAssured.given();
        return request.get(endpoint);
    }

       // Method to make a GET request with form parameters
    public Response getFormRequest(String endpoint, Map<String, String> formParams) {
        RequestSpecification request = RestAssured.given();
        request.queryParams(formParams);
        return request.get(endpoint);
    }

    // Method to make a POST request with JSON body
    public Response postRequest(String endpoint, Object body) {       
        request.contentType("application/json");
        request.body(body);
        return request.post(endpoint);
    }
    

    // Method to make a POST request with form parameters
    public Response postFormRequest(String endpoint, Map<String, String> formParams) {
       
        request.contentType("application/x-www-form-urlencoded");
        request.formParams(formParams);
        return request.post(endpoint);
    }

    
    // Method to make a PUT request with JSON body
    public Response putRequest(String endpoint, Object body) {
      
        request.contentType("application/json");
        request.body(body);
        return request.put(endpoint);
    }

    
    // Method to make a PUT request with form parameters
    public Response putFormRequest(String endpoint, Map<String, String> formParams) {
        
        request.contentType("application/x-www-form-urlencoded");
        request.formParams(formParams);
        return request.put(endpoint);
    }

    // Method to make a DELETE request
    public Response deleteformRequest(String endpoint, Map<String, String> formParams) {        
        request.contentType("application/x-www-form-urlencoded");
        request.formParams(formParams);
        return request.delete(endpoint);
    }

    

}
