package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class UpdateOneAccount extends GenerateBearerToken{
	String baseURI;
	String updateOneAccountEndpoint;
	String updateAccountBodyPath;
	String firstAccountId;
	String readOneAccountEndpoint;
	
		
	public UpdateOneAccount() {
			baseURI = ConfigReader.getProperty("baseURI");
			updateOneAccountEndpoint = ConfigReader.getProperty("updateOneAccountEndpoint");
			updateAccountBodyPath = "src/main/java/data/updateAccountBody.json";
			readOneAccountEndpoint = ConfigReader.getProperty("readOneAccountEndpoint");
		}
	
	@Test(priority=1)
	public void updateOneAccount() {
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
			.body(new File(updateAccountBodyPath))
			.log().all().
		when()
			.put(updateOneAccountEndpoint).
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
		Assert.assertEquals(message, "Account updated successfully.");
	}
	
	@Test(priority=2)
	public void readOneAccount() {
		
		File expectedResponseBody = new File(updateAccountBodyPath);
		JsonPath jp1 = new JsonPath(expectedResponseBody);
		
		String expectedAccountId = jp1.getString("account_id");
		String expectedAccountName = jp1.getString("account_name");
		String expectedDescription = jp1.getString("description");
		String expectedBalance = jp1.getString("balance");
		String expectedAccountNumber = jp1.getString("account_number");
		String expectedContactPerson = jp1.getString("contact_person");
		
		
	Response response =
		
		given()
			.baseUri(baseURI)
			.header("Content-Type","application/json")
			.auth().preemptive().basic("demo1@codefios.com","abc123")
			.queryParam("account_id", expectedAccountId)
			.log().all().
		when()
			.get(readOneAccountEndpoint).
		then()
			.log().all()
			.extract().response();
		
	
		String actualResponseBody = response.getBody().asString();
			
		JsonPath jp = new JsonPath(actualResponseBody);
		String actualAccountName = jp.getString("account_name");
		String actualDescription = jp.getString("description");
		String actualBalance = jp.getString("balance");
		String actualAccountNumber = jp.getString("account_number");
		String actualContactPerson = jp.getString("contact_person");
				
		Assert.assertEquals(actualAccountName, expectedAccountName);
		Assert.assertEquals(actualDescription, expectedDescription);
		Assert.assertEquals(actualBalance, expectedBalance);
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber);
		Assert.assertEquals(actualContactPerson, expectedContactPerson);
		
	}
}
