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

public class UpdateTrainerListSteps {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private static final String TRAINING_DATE = "trainingDate";
  private static final String TRAINING_NAME = "trainingName";
  private static final String TRAINERS_LIST = "trainersList";
  private String username;
  private String trainingDate;
  private String trainingName;
  private List<Map<String, String>> trainersList;
  private String trainersListString;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainees";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;


  @Given("the user enter params for update list of trainers")
  public void theUserEnterParamsForUpdateListOfTrainers(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> firstRow = data.get(0);
    username = firstRow.get(USERNAME);
    trainingDate = firstRow.get(TRAINING_DATE);
    trainingName = firstRow.get(TRAINING_NAME);
    trainersListString = firstRow.get(TRAINERS_LIST);
    trainersList = List.of(Map.of(USERNAME, "TrainerG.TrainerG"));
  }

  @And("prepare token for request for update trainers list for trainee")
  public void prepareTokenForRequestForUpdateTrainersListForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("the user send a PUT request to update list of trainers for trainee")
  public void theUserSendAPUTRequestToUpdateListOfTrainersForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .queryParam(USERNAME, username)
        .queryParam(TRAINING_DATE, trainingDate)
        .queryParam(TRAINING_NAME, trainingName)
        .body(trainersList)
        .put(BASE_URI + "/setting/trainers");
  }

  @Then("response should return status code {int}")
  public void responseShouldReturnStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
