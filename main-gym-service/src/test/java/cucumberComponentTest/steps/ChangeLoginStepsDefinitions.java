package cucumberComponentTest.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import upskill.security.JwtUtils;

import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class ChangeLoginStepsDefinitions {
  private static final String USERNAME = "username";
  private static final String OLD_PASSWORD = "oldPassword";
  private static final String NEW_PASSWORD = "newPassword";
  private static final String AUTHORIZATION = "Authorization";
  private String username;
  private String oldPassword;
  private String newPassword;
  private static final String BASE_URI = "http://localhost:8091/api/v1/users/user";
  private Response response;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;

  @Given("the user enter params for update password")
  public void theUserEnterParamsForUpdatePassword(DataTable dataTable) {
    List<Map<String, String>> userDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = userDataList.get(0);
    username = userData.get(USERNAME);
    oldPassword = userData.get(OLD_PASSWORD);
    newPassword = userData.get(NEW_PASSWORD);
  }

  @And("prepare token for request")
  public void prepareTokenForRequest() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("The user send a PUT request to update")
  public void theUserSendAPUTRequestToUpdate() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .formParam(USERNAME, username)
        .formParam(OLD_PASSWORD, oldPassword)
        .formParam(NEW_PASSWORD, newPassword)
        .put(BASE_URI + "/setting/login");
  }

  @Then("The response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }
}

