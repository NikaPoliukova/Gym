package cucumberIntegrationTest.steps;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.dto.TraineeUpdateRequest;
import upskill.exception.UserNotFoundException;
import upskill.service.TraineeService;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class UpdateTraineeStepDefinitions {
  @Autowired
  private TraineeService traineeService;
  private String username;
  private ResponseEntity<Object> response;
  private TraineeUpdateRequest validTrainee;

  @Given("the valid trainee credentials for update")
  public void givenValidTraineeData() {
    validTrainee = new TraineeUpdateRequest("PAVEL.TRAINEE", "PAVEL",
        "TRAINEE", null, " Street 123", false);
  }
  @Given("the invalid trainee credentials for update")
  public void givenInvalidTraineeData() {
    validTrainee = new TraineeUpdateRequest("invalid", "null",
        "TRAINEE", null, " Street 123", false);
  }

  @When("user submits a request to update the trainee's profile")
  public void whenUserSendsUpdateRequest() {
    try {
      var newTrainee = traineeService.updateTrainee(validTrainee);
      response = ResponseEntity.status(HttpStatus.OK).body(newTrainee);
    } catch (UserNotFoundException e) {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserNotFoundException.class);
    }
  }

  @Then("the response return update information about the trainee")
  public void thenResponseContainsTraineeData() {
    assertNotNull("Expected Trainee", response.getBody());
  }

  @Then("we get status response code {int}")
  public void andResponseStatusIs(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCodeValue());
  }
}
