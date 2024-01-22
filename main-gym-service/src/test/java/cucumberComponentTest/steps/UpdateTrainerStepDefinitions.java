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
import upskill.dto.TrainerUpdateResponse;
import upskill.security.JwtUtils;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UpdateTrainerStepDefinitions {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String SPECIALIZATION = "specialization";
  private static final String IS_ACTIVE = "isActive";
  private String username;
  private String firstName;
  private String lastName;
  private String specialization;
  private boolean isActive;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainers/trainer";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;


  @Given("the user enter params for update trainer profile")
  public void theUserEnterParamsForUpdateTraineeProfile(DataTable dataTable) {
    List<Map<String, String>> trainerDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = trainerDataList.get(0);
    username = userData.get(USERNAME);
    firstName = userData.get(FIRST_NAME);
    lastName = userData.get(LAST_NAME);
    specialization = userData.get(SPECIALIZATION);
    isActive = Boolean.parseBoolean(userData.get(IS_ACTIVE));
  }

  @And("prepare token for request for update trainer")
  public void prepareTokenForRequestForUpdateTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }

  @When("the user send a PUT request to update information about the trainer")
  public void theUserSendAPUTRequestToUpdateInformationAboutTheTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .formParam(USERNAME, username)
        .formParam(FIRST_NAME, firstName)
        .formParam(LAST_NAME, lastName)
        .formParam(SPECIALIZATION, specialization)
        .formParam(IS_ACTIVE, String.valueOf(isActive))
        .put(BASE_URI + "/setting/profile");
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
