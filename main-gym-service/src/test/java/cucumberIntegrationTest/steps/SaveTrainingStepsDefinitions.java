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
import upskill.dto.TrainingRequest;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class SaveTrainingStepsDefinitions {
  private TrainingRequest trainingRequest;
  private Response response;
  private String username = "Trainer.Trainer";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  private String baseUri = "http://localhost:8095/gym-service/api/v1/trainings";

  @Given("the user provides the following training details:")
  public void theUserProvidesTheFollowingTrainingDetails(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> trainingDetails = data.get(0);
    trainingRequest = new TrainingRequest(
        trainingDetails.get("traineeUsername"),
        trainingDetails.get("trainerUsername"),
        trainingDetails.get("trainingName"),
        LocalDate.parse(trainingDetails.get("trainingDate")),
        trainingDetails.get("trainingType"),
        Integer.parseInt(trainingDetails.get("trainingDuration"))
    );
  }

  @And("prepare token for request for save training")
  public void prepareTokenForRequestForSaveTraining() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a POST request for save training and send request to Secondary microservice")
  public void theUserSendAPOSTRequestForSaveTrainingAndSendRequestToSecondaryMicroservice() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .body(trainingRequest)
        .post(baseUri + "/new-training");
  }

  @Then("the response status code should be return {int}")
  public void theResponseStatusCodeShouldBeReturn(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }

  @And("the response body should contain the saved Training object")
  public void theResponseBodyShouldContainTheSavedTrainingObject() {
    assertNotNull("Expected list of training", response.getBody());
  }
}
