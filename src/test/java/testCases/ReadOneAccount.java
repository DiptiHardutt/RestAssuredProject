package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class ReadOneAccount {
	String baseURI;
	String readOneAccountEndpoint;
	
		
	public ReadOneAccount() {
		baseURI = ConfigReader.getProperty("baseURI");
		readOneAccountEndpoint = ConfigReader.getProperty("readOneAccountEndpoint");
		}
	
	@Test
	public void readOneAccount() {
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
			.auth().preemptive().basic("demo1@codefios.com","abc123")
			.queryParam("account_id", "419")
			.log().all().
		when()
			.get(readOneAccountEndpoint).
		then()
			.log().all()
			.extract().response();
		
		int statusCode = response.getStatusCode();
		System.out.println("Status Code is : " + statusCode);
		Assert.assertEquals(statusCode, 200, "Status code is not matched");
		
		String headerContentType = response.getHeader("Content-Type");
		Assert.assertEquals(headerContentType, "application/json", "Content Type is not matching");
	
		long timeInMiliseconds = response.getTimeIn(TimeUnit.MILLISECONDS);
		
		if(timeInMiliseconds<=2000) {
			System.out.println("Response time is within range");
			
		}else {
			System.out.println("Response time is out of range");
		}
		
		String responseBody = response.getBody().asString();
		System.out.println("Response Body: " +responseBody);
		
		JsonPath jp = new JsonPath(responseBody);
		String accountId = jp.getString("account_id");
		
		
	
	}
}
