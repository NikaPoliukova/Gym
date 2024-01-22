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
import upskill.dto.TrainerResponse;
import upskill.security.JwtUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class GetTrainerStepsDefinitions {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private String username;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainers/trainer";

  @Given("the user enter username for get trainer")
  public void theUserEnterUsernameForGetTrainer(DataTable dataTable) {
    List<Map<String, String>> trainerDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = trainerDataList.get(0);
    username = userData.get(USERNAME);
  }

  @And("prepare token for request for trainer")
  public void prepareTokenForRequestForTrainer() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("The user send a GET request to get information about the trainer")
  public void theUserSendAGETRequestToGetInformationAboutTheTrainer() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC)
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .formParam(USERNAME, username)
        .get(BASE_URI);
  }

  @Then("the response contains information about the trainer")
  public void theResponseContainsInformationAboutTheTrainer() {
    assertThat(response.getBody().as(TrainerResponse.class)).isNotNull();
  }

  @Then("we get response status code {int}")
  public void responseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }
}

