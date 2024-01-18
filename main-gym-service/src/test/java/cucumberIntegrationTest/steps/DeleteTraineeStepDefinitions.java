package cucumberIntegrationTest.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.entity.Trainee;
import upskill.exception.UserNotFoundException;
import upskill.service.TraineeService;
import upskill.service.UserService;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class DeleteTraineeStepDefinitions {
  private ResponseEntity<Void> responseEntity;
  private String username;

  private Trainee trainee;
  @Autowired
  private UserService userService;
  @Autowired
  private TraineeService traineeService;

  @Given("the valid trainee credentials for delete trainee")
  public void givenUserExistsWithUsername() {
    username = "DIMA.TRAINEE4";
  }
  @Given("the invalid trainee credentials for delete trainee")
  public void givenInvalidTraineeUsernameForDeleteTrainee() {
    username = "incorrec.tName";
  }

  @When("the user send a DELETE request to delete the trainee")
  public void whenUserSendDeleteRequest() {
    try {
      trainee = traineeService.findByUsername(username);
      userService.delete(trainee.getId());
      responseEntity = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (UserNotFoundException e) {
      responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Then("we get status response code after delete {int}")
  public void thenResponseHasStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, responseEntity.getStatusCodeValue());
  }

}
