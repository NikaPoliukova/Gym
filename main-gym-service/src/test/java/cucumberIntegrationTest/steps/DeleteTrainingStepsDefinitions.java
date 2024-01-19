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
import upskill.dto.TrainingRequestDto;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class DeleteTrainingStepsDefinitions {
  private TrainingRequestDto trainingRequest;
  private String authorizationHeader;
  private Response response;
  private String username = "Trainer.Trainer";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;
  private String baseUri = "http://localhost:8095/gym-service/api/v1/trainings";


  @Given("the user enter params for delete training")
  public void theUserEnterParamsForDeleteTraining(DataTable dataTable) {
    List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
    Map<String, String> trainingDetails = data.get(0);
    trainingRequest = new TrainingRequestDto(
        trainingDetails.get("trainerUsername"),
        trainingDetails.get("trainingName"),
        LocalDate.parse(trainingDetails.get("trainingDate")),
        trainingDetails.get("trainingType"),
        Integer.parseInt(trainingDetails.get("duration")));
  }

  @And("prepare token for request for delete training")
  public void prepareTokenForRequestForDeleteTraining() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a DELETE request for delete training and send request to Secondary microservice")
  public void theUserSendDeleteRequestForDeleteTrainingAndSendRequestToSecondaryMicroservice() {
    response = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .body(trainingRequest)
        .delete(baseUri + "/training");
  }

  @Then("the response code should be return {int}")
  public void theResponseCodeShouldBeReturn(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
