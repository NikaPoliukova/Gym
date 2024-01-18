package cucumberIntegrationTest.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class ToggleTraineeActivationStepsDefinition {
  private Response response;
  private String username;
  private boolean isActive;
  private String baseUri = "http://localhost:8091/api/v1/trainees";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;


  @Given("the user provides the following parameters for toggling Trainee's profile activation")
  public void theUserProvidesTheFollowingParametersForTogglingTraineeSProfileActivation(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> parameters = data.get(0);
    username = parameters.get("username");
    isActive = Boolean.parseBoolean(parameters.get("isActive"));
  }

  @And("prepare token for request for toggling Trainee's profile activation")
  public void prepareTokenForRequestForTogglingTraineeSProfileActivation() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a PATCH request to toggle activation")
  public void theUserSendAPATCHRequestToToggleActivation() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .param("username", username)
        .param("isActive", String.valueOf(isActive))
        .patch(baseUri + "/toggle-activation");
  }

  @Then("response status should be return code {int}")
  public void responseStatusShouldBeReturnCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
