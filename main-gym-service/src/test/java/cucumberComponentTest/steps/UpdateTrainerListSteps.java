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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UpdateTrainerListSteps {

  private String username;
  private String trainingDate;
  private String trainingName;
  private List<Map<String, String>> trainersList;
  private String trainersListString;
  private Response response;
  private String baseUri = "http://localhost:8091/api/v1/trainees";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  @Given("the user enter params for update list of trainers")
  public void theUserEnterParamsForUpdateListOfTrainers(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> firstRow = data.get(0);
    username = firstRow.get("username");
    trainingDate = firstRow.get("trainingDate");
    trainingName = firstRow.get("trainingName");
    trainersListString = firstRow.get("trainersList");
    trainersList = List.of(Map.of("username", "TrainerG.TrainerG"));
  }

  @And("prepare token for request for update trainers list for trainee")
  public void prepareTokenForRequestForUpdateTrainersListForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a PUT request to update list of trainers for trainee")
  public void theUserSendAPUTRequestToUpdateListOfTrainersForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .queryParam("username", username)
        .queryParam("trainingDate", trainingDate)
        .queryParam("trainingName", trainingName)
        .body(trainersList)
        .put(baseUri + "/setting/trainers");
  }

  @Then("response should return status code {int}")
  public void responseShouldReturnStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
