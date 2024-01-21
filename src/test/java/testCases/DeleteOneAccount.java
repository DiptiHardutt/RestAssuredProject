package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class DeleteOneAccount extends GenerateBearerToken{
	String baseURI;
	String deleteOneAccuntEndpoint;
	String deleteAccountId;
	String readOneAccountEndpoint;
	
		
	public DeleteOneAccount() {
			baseURI = ConfigReader.getProperty("baseURI");
			deleteOneAccuntEndpoint = ConfigReader.getProperty("deleteOneAccuntEndpoint");
			readOneAccountEndpoint = ConfigReader.getProperty("readOneAccountEndpoint");
			deleteAccountId="426";
		}
	
	@Test(priority=1)
	public void deleteOneAccount() {
		/*
		  given: all input details -> (baseURI,Header/s,Authorization(basic auth/Bearer Token),
		  		 Payload/Body,QueryParameters)
		  when:  submit api requests-> HttpMethod(Endpoint/Resource)
		  then:  validate response -> (status code, Headers, responseTime, Payload/Body)
		 */ 
	Response response =
		
		given()
			.baseUri(baseURI)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer " + bearerToken)
			.queryParam("account_id", deleteAccountId)
			.log().all().
		when()
			.delete(deleteOneAccuntEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code is : " + statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not matched");
		
		String headerContentType = response.getHeader("Content-Type");
		Assert.assertEquals(headerContentType, "application/json", "Content Type is not matching");
	
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " +responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String message = jp.getString("message");
		Assert.assertEquals(message, "Account deleted successfully.");
	}
	
	@Test(priority=2)
	public void readOneAccount() {
		
		
		
	Response response =
		
		given()
			.baseUri(baseURI)
			.header("Content-Type","application/json")
			.auth().preemptive().basic("demo1@codefios.com","abc123")
			.queryParam("account_id", deleteAccountId)
			.log().all().
		when()
			.get(readOneAccountEndpoint).
		then()
			.log().all()
			.extract().response();
	int statusCode = response.getStatusCode();
	System.out.println("Status Code is : " + statusCode);
	Assert.assertEquals(statusCode, 404, "Status code is not matched");
	
	
		String actualResponseBody = response.getBody().asString();
		
			
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualmessage = jp.getString("message");
		
				
		Assert.assertEquals(actualmessage, "No Record Found");
				
	}
}
