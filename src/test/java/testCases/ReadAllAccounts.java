package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import util.ConfigReader;

import static io.restassured.RestAssured.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class ReadAllAccounts extends GenerateBearerToken{
	String baseURI;
	String readAllAccountsEndpoint;
	String firstAccountId;
	
	public ReadAllAccounts() {
		
		baseURI = ConfigReader.getProperty("baseURI");
		readAllAccountsEndpoint = ConfigReader.getProperty("readAllAccountsEndpoint");
		
	}
	@Test
	public void readAllAccounts() {
		 /*
		  given: all input details -> (baseURI,Header/s,Authorization,Payload/Body,QueryParameters)
		  when:  submit api requests-> HttpMethod(Endpoint/Resource)
		  then:  validate response -> (status code, Headers, responseTime, Payload/Body)
		   
		  baseURI=https://qa.codefios.com/api  
		  https://qa.codefios.com/api /user/login
		  Headers: 
		  "Content-Type" = "application/json"
		  payload/body=
		  {
		      "username": "admin",
		      "password": "123456"
		  }
		  statusCode=201
		  response=
		  {
		      "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOiIyIiwidXNlcm5hbWUiOiJhZG1pbiIsIkFQSV9USU1FIjoxNzA1MjQ1NjYyfQ.qUeFzu-fkB4ZkYhQNqV7cmNCahLYQWLZcQEa3asVMBU",
		      "status": true,
		      "message": "Login success!",
		      "token_expire_time": 86400
		  }
		  */ 
		
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
		firstAccountId = jp.getString("records[0].account_id");
		System.out.println("First Account Id : " + firstAccountId);
		if(firstAccountId!=null) {
			System.out.println("First account id is not null");
		}else {
			System.out.println("First account id is null");
		}
		
	
	}
}
