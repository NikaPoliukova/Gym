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
import upskill.dto.TrainerUpdateResponse;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UpdateTrainerStepDefinitions {

  private String username;
  private String firstName;
  private String lastName;
  private String specialization;
  private boolean isActive;
  private Response response;
  private String baseUri = "http://localhost:8091/api/v1/trainers/trainer";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  @Given("the user enter params for update trainer profile")
  public void theUserEnterParamsForUpdateTraineeProfile(DataTable dataTable) {
    List<Map<String, String>> trainerDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = trainerDataList.get(0);
    username = userData.get("username");
    firstName = userData.get("firstName");
    lastName = userData.get("lastName");
    specialization = userData.get("specialization");
    isActive = Boolean.parseBoolean(userData.get("isActive"));
  }

  @And("prepare token for request for update trainer")
  public void prepareTokenForRequestForUpdateTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a PUT request to update information about the trainer")
  public void theUserSendAPUTRequestToUpdateInformationAboutTheTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .formParam("firstName", firstName)
        .formParam("lastName", lastName)
        .formParam("specialization", specialization)
        .formParam("isActive", String.valueOf(isActive))
        .put(baseUri + "/setting/profile");
  }

  @And("the response body should contain TrainerUpdateResponse object")
  public void theResponseBodyShouldContainTrainerUpdateResponseObject() {
    assertThat(response.getBody().as(TrainerUpdateResponse.class)).isNotNull();
  }

  @Then("we get response which should return status code {int}")
  public void theResponseShouldReturnStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
