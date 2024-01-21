package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class CreateOneAccount extends GenerateBearerToken{
	String baseURI;
	String createOneAccountEndpoint;
	String createAccountBodyPath;
	String readAllAccountsEndpoint;
	String firstAccountId;
	String readOneAccountEndpoint;
	SoftAssert softAssert;
	
		
	public CreateOneAccount() {
			baseURI = ConfigReader.getProperty("baseURI");
			createOneAccountEndpoint = ConfigReader.getProperty("createOneAccountEndpoint");
			createAccountBodyPath = "src/main/java/data/createAccountBody.json";
			readAllAccountsEndpoint = ConfigReader.getProperty("readAllAccountsEndpoint");
			readOneAccountEndpoint = ConfigReader.getProperty("readOneAccountEndpoint");
			softAssert = new SoftAssert();
		}
	
	@Test(priority=1)
	public void createOneAccount() {
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
			.body(new File(createAccountBodyPath))
			.log().all().
		when()
			.post(createOneAccountEndpoint).
		then()
			.log().all()
			.extract().response();
		//Soft Assertion example
		int statusCode = response.getStatusCode();
		System.out.println("Status Code is : " + statusCode);
//		Assert.assertEquals(statusCode, 201, "Status code is not matched");
		softAssert.assertEquals(statusCode, 200, "Status code is not matched");
		
		String headerContentType = response.getHeader("Content-Type");
//		Assert.assertEquals(headerContentType, "application/json", "Content Type is not matching");
		softAssert.assertEquals(headerContentType, "application/json", "Content Type is not matching");
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " +responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String message = jp.getString("message");
//		Assert.assertEquals(message, "Account created successfully.");
		softAssert.assertEquals(message, "Account created successfully.");
		softAssert.assertAll();
	}
	@Test(priority=2)
	public void readAllAccounts() {
		
	Response response =
		
		given()
			.baseUri(baseURI)
			.header("Content-Type","application/json")
			.header("Authorization","Bearer "+generateBearerToken()).
//			.log().all().
		when()
			.get(readAllAccountsEndpoint).
		then()
//			.log().all()
			.extract().response();
		
		
		String responseBody = response.getBody().asString();
			
		JsonPath jp = new JsonPath(responseBody);
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("First Account Id :"+firstAccountId);
	}
	
	@Test(priority=3)
	public void readOneAccount() {
		
	Response response =
		
		given()
			.baseUri(baseURI)
			.header("Content-Type","application/json")
			.auth().preemptive().basic("demo1@codefios.com","abc123")
			.queryParam("account_id", firstAccountId)
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
		
		File expectedResponseBody = new File(createAccountBodyPath);
		JsonPath jp1 = new JsonPath(expectedResponseBody);
		String expectedAccountName = jp1.getString("account_name");
		String expectedDescription = jp1.getString("description");
		String expectedBalance = jp1.getString("balance");
		String expectedAccountNumber = jp1.getString("account_number");
		String expectedContactPerson = jp1.getString("contact_person");
		
		Assert.assertEquals(actualAccountName, expectedAccountName);
		Assert.assertEquals(actualDescription, expectedDescription);
		Assert.assertEquals(actualBalance, expectedBalance);
		Assert.assertEquals(actualAccountNumber, expectedAccountNumber);
		Assert.assertEquals(actualContactPerson, expectedContactPerson);
		
	}
}
