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

public class FindingTrainingsForTraineeStepsDefinition {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private static final String USERNAME1 = "username";
  private static final String PERIOD_FROM = "periodFrom";
  private static final String PERIOD_TO = "periodTo";
  private static final String TRAINER_NAME = "trainerName";
  private static final String TRAINING_TYPE = "trainingType";

  private String username;
  private LocalDate periodFrom;
  private LocalDate periodTo;
  private String trainerName;
  private String trainingType;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainees";

  @Given("the user provides the following parameters for finding trainings")
  public void theUserProvidesTheFollowingParametersForFindingTrainings(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> parameters = data.get(0);
    username = parameters.get(USERNAME);
    periodFrom = LocalDate.parse(parameters.get(PERIOD_FROM));
    periodTo = LocalDate.parse(parameters.get(PERIOD_TO));
    trainerName = parameters.get(TRAINER_NAME);
    trainingType = parameters.get(TRAINING_TYPE);
  }

  @And("prepare token for request for get trainers list for trainee")
  public void prepareTokenForRequestForGetTrainersListForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("send a GET request to find trainers list for trainee")
  public void sendGetRequestToFindTrainersListForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .param(USERNAME1, username)
        .param(PERIOD_FROM, periodFrom.toString())
        .param(PERIOD_TO, periodTo.toString())
        .param(TRAINER_NAME, trainerName)
        .param(TRAINING_TYPE, trainingType)
        .get(BASE_URI + "/trainings");
  }

  @Then("the response contains list of training for trainee")
  public void theResponseContainsListOfTrainingForTrainee() {
    assertNotNull("Expected list of training", response.getBody());
  }

  @Then("after should be response status code {int}")
  public void afterShouldBeResponseStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
