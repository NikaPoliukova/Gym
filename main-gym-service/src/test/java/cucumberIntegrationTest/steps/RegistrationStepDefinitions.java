package cucumberIntegrationTest.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.dto.Principal;
import upskill.dto.TraineeRegistration;
import upskill.dto.TrainerRegistration;
import upskill.service.TraineeService;
import upskill.service.TrainerService;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationStepDefinitions {

  private ResponseEntity<Principal> response;
  private Response badResponse;
  private TraineeRegistration validTraineeRegistration;
  private Principal principal;

  private TraineeRegistration invalidTraineeRegistration;
  private TrainerRegistration invalidTrainerRegistration;
  private TrainerRegistration validTrainerRegistration;
  @Autowired
  private TraineeService traineeService;
  @Autowired
  private TrainerService trainerService;

  @Given("the trainee registration request with valid data")
  public void givenTraineeRegistrationRequestWithValidData() {
    validTraineeRegistration = new TraineeRegistration("OLA", "TRAINEE", "Street 1",
        LocalDate.of(1992, 2, 13));
  }

  @Given("the trainer registration request with valid data")
  public void givenTrainerRegistrationRequestWithValidData() {
    validTrainerRegistration = new TrainerRegistration("Jon", "Trainer", "YOGA");
  }

  @Given("the trainee registration request with invalid data")
  public void givenTraineeRegistrationRequestWithInvalidData() {
    invalidTraineeRegistration = new TraineeRegistration(null, null, "Street 1",
        null);
  }

  @Given("the trainer registration request with invalid data")
  public void givenTrainerRegistrationRequestWithInvalidData() {
    invalidTrainerRegistration = new TrainerRegistration("Jon", "Trainer", null);
  }

  @When("the trainee registration API saved with valid data")
  public void whenTraineeRegistrationApiIsCalledWithValidData() {
    principal = traineeService.saveTrainee(validTraineeRegistration);
    response = ResponseEntity.status(HttpStatus.CREATED).body(principal);
  }

  @When("the trainer registration API saved with valid data")
  public void whenTrainerRegistrationApiIsCalledWithValidData() {
    principal = trainerService.saveTrainer(validTrainerRegistration);
    response = ResponseEntity.status(HttpStatus.CREATED).body(principal);
  }

  @When("I submit the trainee registration request")
  public void whenTraineeRegistrationApiIsCalledWithInvalidData() {
    badResponse = RestAssured.given()
        .contentType(ContentType.JSON)
        .header("Jenkins-Crumb", "")
        .body(invalidTraineeRegistration)
        .post("/api/v1/registration/trainee");
  }


  @When("I submit the trainer registration request")
  public void whenTrainerRegistrationApiIsCalledWithInvalidData() {
    badResponse = RestAssured.given()
        .contentType(ContentType.JSON)
        .header("Jenkins-Crumb", "")
        .body(invalidTrainerRegistration)
        .post("/api/v1/registration/trainer");
  }

  @Then("the response status code should be {int}")
  public void thenResponseStatusCodeShouldBe(int expectedStatusCode) {
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
  }

  @Then("the response should have status code {int}")
  public void the_response_should_contain_a_validation_error_message(int expectedStatusCode) {
    int actualStatusCode = badResponse.getStatusCode();
    assertEquals(expectedStatusCode, actualStatusCode);
  }

  @And("the response should contain the trainee's details")
  public void andResponseShouldContainTraineeDetails() {
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
  }

  @And("the response should contain the trainer's details")
  public void andResponseShouldContainTrainerDetails() {
    assertNotNull(response.getBody());
    assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
  }


}
