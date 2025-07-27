package coreFunctions;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.*;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.lang.reflect.Method;

public class ApiTests {

    ExtentReports extent;
    ExtentTest test;

    static String authToken; // token for all tests

    @BeforeClass
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("ApiTestReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Mzna");
        extent.setSystemInfo("Environment", "QA");
    }

    @BeforeMethod
    public void startTest(Method method) {
        Test testAnnotation = method.getAnnotation(Test.class);
        String description = (testAnnotation != null && !testAnnotation.description().isEmpty()) ?
                testAnnotation.description() : method.getName();
        test = extent.createTest(description);
    }

    @AfterClass
    public void tearDownReport() {
        extent.flush();
    }

    // ======= Positive tests =======

    @Test(description = "API Test: Login with valid credentials")
    public void testLoginAPI() {
        // test login with right email and password, get token if success
        RestAssured.baseURI = "http://localhost:4000";

        try {
            test.info(" Step 1: Sending POST request to /api/users/login with valid credentials");

            String payload = "{ \"email\": \"mznaat188@gmail.com\", \"password\": \"mzna123\" }";

            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .when()
                    .post("/api/users/login")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info("Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 200, "Expected 200 but got " + statusCode);
            test.pass("Status code is 200 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

            authToken = response.jsonPath().getString("token");
            Assert.assertNotNull(authToken, "Token should not be null");
            test.pass("Response contains token, token extracted successfully");

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in API test: " + e.getMessage());
        }
    }

    @Test(description = "API Test: GET /api/todos with valid token - Positive scenario", dependsOnMethods = {"testLoginAPI"})
    public void testGetAllTodos() {
        // test get all todos with correct token, expect success
        RestAssured.baseURI = "http://localhost:4000";

        try {
            test.info(" Step 1: Sending GET request to /api/todos with valid token");

            Response response = given()
                    .header("Authorization", "Bearer " + authToken)
                    .when()
                    .get("/api/todos")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 200, "Expected 200 but got " + statusCode);
            test.pass("Status code is 200 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in API test: " + e.getMessage());
        }
    }

    @Test(description = "API Test: POST /api/todos to add new todo", dependsOnMethods = {"testLoginAPI"})
    public void testAddNewTodo() {
        // test add new todo with valid token, expect created success
        RestAssured.baseURI = "http://localhost:4000";

        String newTodo = """
        {
            "title": "New Todo from API",
            "description": "Testing POST new todo"
        }
        """;

        try {
            test.info(" Step 1: Sending POST request to /api/todos with valid token and new todo data");

            Response response = given()
                    .header("Authorization", "Bearer " + authToken)
                    .header("Content-Type", "application/json")
                    .body(newTodo)
                    .when()
                    .post("/api/todos")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 201, "Expected 201 but got " + statusCode);
            test.pass("Status code is 201 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in API test: " + e.getMessage());
        }
    }

    @Test(description = "Positive API Test: DELETE /api/todos/:id with valid token and existing todo", dependsOnMethods = {"testLoginAPI"})
    public void testDeleteTodoSuccess() {
        // test deletetodo with valid token, expect success status
        RestAssured.baseURI = "http://localhost:4000";

        String todoId = "68863f5cc99d8e5dc43ce39d";

        try {
            test.info(" Step 1: Sending DELETE request to /api/todos/" + todoId + " with valid token");

            Response response = given()
                    .header("Authorization", "Bearer " + authToken)
                    .when()
                    .delete("/api/todos/" + todoId)
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertTrue(statusCode == 200 || statusCode == 204, "Expected 200 or 204 but got " + statusCode);
            test.pass("Status code indicates successful deletion");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in positive DELETE API test: " + e.getMessage());
        }
    }

    @Test(description = "Positive API Test: PUT /api/todos/:id - Successful update", dependsOnMethods = {"testLoginAPI"})
    public void testUpdateTodoSuccess() {
        // test update todo with valid token, expect success status
        RestAssured.baseURI = "http://localhost:4000";

        String todoId = "6885f4afc99d8e5dc43ce158";

        String updatedTodoJson = """
        {
            "title": "Updated Title",
            "description": "Updated description",
            "done": true
        }
        """;

        try {
            test.info(" Step 1: Sending PUT request to /api/todos/" + todoId + " with valid token");

            Response response = given()
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(ContentType.JSON)
                    .body(updatedTodoJson)
                    .when()
                    .put("/api/todos/" + todoId)
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertTrue(statusCode == 200 || statusCode == 204, "Expected 200 or 204 but got " + statusCode);
            test.pass("Status code indicates successful update");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in positive PUT API test: " + e.getMessage());
        }
    }


    // ======= Negative tests =======

    @Test(description = "Negative API Test: Login with empty password")
    public void testLoginWithEmptyPassword() {
        // test login with empty password, expect error message
        RestAssured.baseURI = "http://localhost:4000";

        try {
            test.info("Sending POST request to /api/users/login with empty password");
            test.info("Expected Result: Status code 400 or 401 with error message");

            String payload = "{ \"email\": \"mznaat188@gmail.com\", \"password\": \"\" }";

            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(payload)
                    .when()
                    .post("/api/users/login")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info("Received status code: " + statusCode);

            Assert.assertTrue(statusCode == 400 || statusCode == 401,
                    "Expected 400 or 401 but got " + statusCode);
            test.pass("Status code is as expected");

            String responseBody = response.getBody().asString();
            test.info("Response body: " + responseBody);

            Assert.assertTrue(responseBody.toLowerCase().contains("password") ||
                            responseBody.toLowerCase().contains("error"),
                    "Expected error message about password");

            test.pass("Response body contains error message about password");

        } catch (Exception e) {
            test.fail("Exception occurred: " + e.getMessage());
            Assert.fail("Exception in API test: " + e.getMessage());
        }
    }


    @Test(description = "Simplified Negative API Test: GET /api/todos without token")
    public void testGetTodosWithoutTokenSimplified() {
        // test get todos without token, print response to see error
        RestAssured.baseURI = "http://localhost:4000";

        Response response = given()
                .when()
                .get("/api/todos")
                .then()
                .extract().response();

        int statusCode = response.getStatusCode();
        System.out.println("Status code: " + statusCode);
        System.out.println("Response body: " + response.getBody().asString());

        // no assert, just print to check server response
    }


    @Test(description = "Negative API Test: POST /api/todos without or invalid token")
    public void testAddNewTodoWithoutToken() {
        // test add todo without token, expect 401 error
        RestAssured.baseURI = "http://localhost:4000";

        String newTodo = """
        {
            "title": "New Todo",
            "description": "Testing POST without token"
        }
        """;

        try {
            test.info(" Step 1: Sending POST request to /api/todos without token");

            Response response = given()
                    .header("Content-Type", "application/json")
                    .body(newTodo)
                    .when()
                    .post("/api/todos")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 401, "Expected 401 but got " + statusCode);
            test.pass("Status code is 401 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in negative API test: " + e.getMessage());
        }
    }

    @Test(description = "Negative API Test: DELETE /api/todos/:id with invalid token")
    public void testDeleteTodoWithInvalidToken() {
        // test delete todo with invalid token, expect 401 error
        RestAssured.baseURI = "http://localhost:4000";

        String todoId = "68862bd1c99d8e5dc43ce37";

        try {
            test.info(" Step 1: Sending DELETE request to /api/todos/" + todoId + " with invalid token");

            Response response = given()
                    .header("Authorization", "Bearer invalid_or_expired_token_here")
                    .when()
                    .delete("/api/todos/" + todoId)
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 401, "Expected 401 but got " + statusCode);
            test.pass("Status code is 401 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in negative DELETE API test: " + e.getMessage());
        }
    }

    @Test(description = "Negative API Test: DELETE /api/todos/:id with valid todo")
    public void testDeleteTodoWithoutToken() {
        // test delete todo without token, expect 401 error
        RestAssured.baseURI = "http://localhost:4000";

        String todoId = "68862bd1c99d8e5dc43ce37";

        try {
            test.info(" Step 1: Sending DELETE request to /api/todos/" + todoId + " without token");

            Response response = given()
                    .when()
                    .delete("/api/todos/" + todoId)
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertEquals(statusCode, 401, "Expected 401 but got " + statusCode);
            test.pass("Status code is 401 as expected");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in negative DELETE API test: " + e.getMessage());
        }
    }

    @Test(description = "Negative API Test: PUT /api/todos/:id - Update should fail due to server bug")
    public void testUpdateTodoExpectFailure() {
        // test update todo but expect fail due to API bug
        RestAssured.baseURI = "http://localhost:4000";

        String todoId = "68862bd1c99d8e5dc43ce377";
        String updatedBody = "{ \"title\": \"Updated Title\", \"description\": \"Updated Description\" }";

        try {
            test.info(" Step 1: Sending PUT request to /api/todos/" + todoId + " to update todo");

            Response response = given()
                    .header("Authorization", "Bearer " + authToken)
                    .contentType(ContentType.JSON)
                    .body(updatedBody)
                    .when()
                    .put("/api/todos/" + todoId)
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            test.info(" Step 2: Received status code: " + statusCode);

            Assert.assertTrue(statusCode >= 400, "Expected failure status code but got " + statusCode);
            test.pass("Received failure status code as expected due to known API issue");

            String responseBody = response.getBody().asString();
            test.info(" Step 3: Response body: " + responseBody);

        } catch (Exception e) {
            test.fail(" Exception occurred: " + e.getMessage());
            Assert.fail("Exception in negative PUT API test: " + e.getMessage());
        }
    }
}
