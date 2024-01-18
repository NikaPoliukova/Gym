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
import upskill.dto.TraineeUpdateResponse;
import upskill.security.JwtUtils;
import upskill.service.TraineeService;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class UpdateTraineeStepDefinitions {
  @Autowired
  private TraineeService traineeService;
  private String username;
  private String firstName;
  private String lastName;
  private LocalDate dateOfBirth;
  private String address;
  private boolean isActive;
  private Response response;
  private String baseUri = "http://localhost:8091/api/v1/trainees";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  @Given("the user enter params for update trainee profile")
  public void theUserEnterParamsForUpdateTraineeProfile(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = traineeDataList.get(0);
    username = userData.get("username");
    firstName = userData.get("firstName");
    lastName = userData.get("lastName");
    dateOfBirth = LocalDate.parse(userData.get("dateOfBirth"));
    address = userData.get("address");
    isActive = Boolean.parseBoolean(userData.get("isActive"));
  }

  @And("prepare token for request for update trainee")
  public void prepareTokenForRequestForUpdateTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a PUT request to update information about the trainee")
  public void theUserSendAPUTRequestToUpdateInformationAboutTheTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .formParam("firstName", firstName)
        .formParam("lastName", lastName)
        .formParam("dateOfBirth", dateOfBirth.toString())
        .formParam("address", address)
        .formParam("isActive", String.valueOf(isActive))
        .put(baseUri + "/trainee/setting/profile");
  }


  @And("the response body should contain TraineeUpdateResponse object")
  public void theResponseBodyShouldContainTraineeUpdateResponseObject() {
    assertThat(response.getBody().as(TraineeUpdateResponse.class)).isNotNull();
  }

  @Then("the response should return status code {int}")
  public void theResponseShouldReturnStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }

//  @Given("the valid trainee credentials for update")
//  public void givenValidTraineeData() {
//    validTrainee = new TraineeUpdateRequest("PAVEL.TRAINEE", "PAVEL",
//        "TRAINEE", null, " Street 123", false);
//  }
//  @Given("the invalid trainee credentials for update")
//  public void givenInvalidTraineeData() {
//    validTrainee = new TraineeUpdateRequest("invalid", "null",
//        "TRAINEE", null, " Street 123", false);
//  }
//
//  @When("user submits a request to update the trainee's profile")
//  public void whenUserSendsUpdateRequest() {
//    try {
//      var newTrainee = traineeService.updateTrainee(validTrainee);
//      response = ResponseEntity.status(HttpStatus.OK).body(newTrainee);
//    } catch (UserNotFoundException e) {
//      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserNotFoundException.class);
//    }
//  }
//
//  @Then("the response return update information about the trainee")
//  public void thenResponseContainsTraineeData() {
//    assertNotNull("Expected Trainee", response.getBody());
//  }
//
//  @Then("we get status response code {int}")
//  public void andResponseStatusIs(int expectedStatus) {
//    assertEquals("Expected status code", expectedStatus, response.getStatusCodeValue());
//  }

}
