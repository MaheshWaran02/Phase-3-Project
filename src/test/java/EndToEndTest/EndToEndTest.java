package EndToEndTest;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	Response response;
	String EndPoint = "http://localhost:8088/employees";

	@Test
	public void EndToEndTest1() {

		// Get Call
		Response response = GetEmployee();
		Assert.assertEquals(200, response.getStatusCode());
		String body = response.getBody().asString();
		System.out.println("Response Body is " + body);

		response = PostEmployee("John", "Walk", "20000", "Awc3697@gamil.com");
		Assert.assertEquals(201, response.getStatusCode());
		String name = response.jsonPath().getString("firstName");
		Assert.assertEquals("John", name);
		JsonPath jpath = response.jsonPath();
		int EmployeeID = jpath.get("id");
		System.out.println("Employee id : " + EmployeeID);

		response = updateEmployee(EmployeeID);
		Assert.assertEquals(response.getStatusCode(), 200);
		String employeeName = response.jsonPath().getString("firstName");
		System.out.println(employeeName);
		Assert.assertEquals(employeeName, "Tom");

		response = DeleteTheEmployee(EmployeeID);
		Assert.assertEquals(response.getStatusCode(), 200);

		response = SingleEmplouee(EmployeeID);
		Assert.assertEquals(400, response.getStatusCode());
		String body1 = response.getBody().asString();
		System.out.println("Response Body is " + body1);

		response = GetEmployee();
		Assert.assertEquals(200, response.getStatusCode());
		String body2 = response.getBody().asString();
		System.out.println("Response Body is " + body2);

	}

	public Response GetEmployee() {

		RestAssured.baseURI = EndPoint;
		RequestSpecification request = RestAssured.given();
		Response response = request.get();
		System.out.println("Response code is " + response.statusCode());
		return response;

	}

	public Response PostEmployee(String name, String lastName, String salary, String email) {
		HashMap<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("firstName", name);
		requestBody.put("lastName", lastName);
		requestBody.put("salary", salary);
		requestBody.put("email", email);
		RestAssured.baseURI = EndPoint;
		RequestSpecification request = RestAssured.given();
		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(requestBody).post();
		String body2 = response.getBody().asString();
		System.out.println("The response body is : " + body2);
		System.out.println("Response code is " + response.statusCode());
		return response;
	}

	public Response updateEmployee(int employeeID) {
		HashMap<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("firstName", "Tom");
		requestBody.put("lastName", "Show");
		requestBody.put("salary", "3697");
		requestBody.put("email", "Tom@gmail.com");

		RestAssured.baseURI = EndPoint;
		RequestSpecification request = RestAssured.given();

		Response response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(requestBody)
				.put("/" + employeeID);

		String body = response.getBody().asString();
		System.out.println("Response body is " + body);
		System.out.println("Response code is " + response.statusCode());

		return response;
	}

	public Response DeleteTheEmployee(int employeeID) {
		RestAssured.baseURI = EndPoint;
		RequestSpecification request = RestAssured.given();
		Response response = request.delete("/" + employeeID);

		String body = response.getBody().asString();
		System.out.println("Response body is " + body);
		System.out.println("Response code is " + response.statusCode());
		return response;
	}

	public Response SingleEmplouee(int employeeID) {

		RestAssured.baseURI = EndPoint;
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/" + employeeID);
		System.out.println("Response code is " + response.statusCode());
		return response;
	}
}
