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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class FindingTrainingsForTrainerStepsDefinition {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private static final String PERIOD_FROM = "periodFrom";
  private static final String PERIOD_TO = "periodTo";
  private static final String TRAINEE_NAME = "traineeName";
  private String username;
  private LocalDate periodFrom;
  private LocalDate periodTo;
  private String traineeName;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainers/trainer";

  @Given("the user enter params for finding trainings for trainer")
  public void theUserEnterParamsForFindingTrainings(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> parameters = data.get(0);
    username = parameters.get(USERNAME);
    periodFrom = LocalDate.parse(parameters.get(PERIOD_FROM));
    periodTo = LocalDate.parse(parameters.get(PERIOD_TO));
    traineeName = parameters.get(TRAINEE_NAME);
  }

  @And("prepare token for request for get trainers list for trainer")
  public void prepareTokenForRequestForGetTrainersListForTrainer() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("send a GET request to find training list for trainer")
  public void sendGetRequestToFindTrainersListForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .param(USERNAME, username)
        .param(PERIOD_FROM, periodFrom.toString())
        .param(PERIOD_TO, periodTo.toString())
        .param(TRAINEE_NAME, traineeName)
        .get(BASE_URI + "/trainings");
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
