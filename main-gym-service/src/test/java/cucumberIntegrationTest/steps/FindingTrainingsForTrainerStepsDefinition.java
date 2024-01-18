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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class FindingTrainingsForTrainerStepsDefinition {
  private String username;
  private LocalDate periodFrom;
  private LocalDate periodTo;
  private String traineeName;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;
  private Response response;
  private String baseUri = "http://localhost:8091/api/v1/trainers/trainer";

  @Given("the user enter params for finding trainings for trainer")
  public void theUserEnterParamsForFindingTrainings(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> parameters = data.get(0);
    username = parameters.get("username");
    periodFrom = LocalDate.parse(parameters.get("periodFrom"));
    periodTo = LocalDate.parse(parameters.get("periodTo"));
    traineeName = parameters.get("traineeName");
  }

  @And("prepare token for request for get trainers list for trainer")
  public void prepareTokenForRequestForGetTrainersListForTrainer() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("send a GET request to find training list for trainer")
  public void sendGetRequestToFindTrainersListForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .param("username", username)
        .param("periodFrom", periodFrom.toString())
        .param("periodTo", periodTo.toString())
        .param("traineeName", traineeName)
        .get(baseUri + "/trainings");
  }

  @Then("the response contains list of training for trainer")
  public void theResponseContainsListOfTrainingForTrainee() {
    assertNotNull("Expected list of training", response.getBody());
  }

  @Then("after we should be response status code {int}")
  public void afterShouldBeResponseStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
