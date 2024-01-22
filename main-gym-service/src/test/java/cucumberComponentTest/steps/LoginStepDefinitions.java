package cucumberComponentTest.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;


public class LoginStepDefinitions {
  private static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  private String username;
  private String password;
  private static final String BASE_URI = "http://localhost:8091";
  private Response response;

  @Given("The user enters login information")
  public void theUserEntersCorrectLoginInformation(DataTable dataTable) {
    List<Map<String, String>> loginDataList = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> loginData : loginDataList) {
      username = loginData.get(USERNAME);
      password = loginData.get(PASSWORD);
    }
  }

  @When("the user makes a POST request for login")
  public void theUserMakesAPOSTRequestForLogin() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC)
        .formParam(USERNAME, username)
        .formParam(PASSWORD, password)
        .post(BASE_URI + "/login");
  }

  @Then("the user get status {int}")
  public void theUserIsSuccessfullyAuthorization(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }

  @And("generated access token and set to header")
  public void generatedAccessTokenAndSetToHeader() {
    Assert.assertNotNull("expected access token", response.getHeader("Authorization"));
  }
}
