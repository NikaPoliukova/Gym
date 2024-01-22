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

public class ToggleTraineeActivationStepsDefinition {
  private static final String USERNAME = "username";
  private static final String IS_ACTIVE = "isActive";
  private static final String AUTHORIZATION = "Authorization";
  private Response response;
  private String username;
  private boolean isActive;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainees";

  @Autowired
  private JwtUtils jwtUtils;
  private String token;

  @Given("the user provides the following parameters for toggling Trainee's profile activation")
  public void theUserProvidesTheFollowingParametersForTogglingTraineeSProfileActivation(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> parameters = data.get(0);
    username = parameters.get(USERNAME);
    isActive = Boolean.parseBoolean(parameters.get(IS_ACTIVE));
  }

  @And("prepare token for request for toggling Trainee's profile activation")
  public void prepareTokenForRequestForTogglingTraineeSProfileActivation() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("the user send a PATCH request to toggle activation")
  public void theUserSendAPATCHRequestToToggleActivation() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .param(USERNAME, username)
        .param(IS_ACTIVE, String.valueOf(isActive))
        .patch(BASE_URI + "/toggle-activation");
  }

  @Then("response status should be return code {int}")
  public void responseStatusShouldBeReturnCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
