package cucumberComponentTest.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import upskill.dto.TraineeRegistration;
import upskill.dto.TrainerRegistration;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class RegistrationStepDefinitions {

  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String ADDRESS = "address";
  private static final String DATE_OF_BIRTH = "dateOfBirth";
  private static final String SPECIALIZATION = "specialization";
  private static final String USERNAME = "username";
  public static final String PASSWORD = "password";

  private Response response;
  private TraineeRegistration validTraineeRegistration;
  private TrainerRegistration validTrainerRegistration;
  private static final String BASE_URI = "http://localhost:8091/api/v1/registration";

  @Given("the user provides the following trainee registration data")
  public void theUserProvidesTheFollowingTraineeRegistrationData(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> traineeData : traineeDataList) {
      var firstName = traineeData.get(FIRST_NAME);
      var lastName = traineeData.get(LAST_NAME);
      var address = traineeData.get(ADDRESS);
      var dateOfBirth = LocalDate.parse(traineeData.get(DATE_OF_BIRTH));
      validTraineeRegistration = new TraineeRegistration(firstName, lastName, address, dateOfBirth);
    }
  }

  @Given("the user provides the following trainer registration data")
  public void theUserProvidesTheFollowingTrainerRegistrationData(DataTable dataTable) {
    List<Map<String, String>> trainerDataList = dataTable.asMaps(String.class, String.class);
    for (Map<String, String> trainerData : trainerDataList) {
      var firstName = trainerData.get(FIRST_NAME);
      var lastName = trainerData.get(LAST_NAME);
      var specialization = trainerData.get(SPECIALIZATION);
      validTrainerRegistration = new TrainerRegistration(firstName, lastName, specialization);
    }
  }

  @When("the user makes a POST request for save trainee")
  public void theUserMakesAPOSTRequestToTrainee() {
    response = RestAssured
        .given()
        .contentType("application/json")
        .body(validTraineeRegistration)
        .post(BASE_URI + "/trainee");
  }

  @When("the user makes a POST request for save trainer")
  public void theUserMakesAPOSTRequestForSaveTrainer() {
    response = RestAssured
        .given()
        .contentType("application/json")
        .body(validTrainerRegistration)
        .post(BASE_URI + "/trainer");
  }

  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }

  @And("the response should contain Principal object")
  public void theResponseShouldContainPrincipalObjectWithUsernameAndPassword() {
    assertNotNull(response.getBody());
    Assert.assertNotNull("Username should be present", response.jsonPath().get(USERNAME));
    Assert.assertNotNull("Password should be present", response.jsonPath().get(PASSWORD));
  }
}
