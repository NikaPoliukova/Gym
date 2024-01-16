package cucumberIntegrationTest.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.entity.Trainer;
import upskill.exception.UserNotFoundException;
import upskill.service.TraineeService;
import upskill.service.TrainingService;
import upskill.service.UserService;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class FindNotActiveTrainersStepDefinitions {
  private ResponseEntity<Object> response;
  private String username;
  @Autowired
  private TrainingService trainingService;
  @Autowired
  private UserService userService;
  @Autowired
  private TraineeService traineeService;
  private List<Trainer> list;

  @Given("the trainee's username for finding trainers")
  public void givenTraineeExistsWithUsername() {
    username = "Trainee.Trainee";
  }

  @When("sends a GET request to find inactive trainers for the trainee")
  public void whenUserSendsGetRequestToFindNotActiveTrainers() {
    try {
      list = trainingService.findNotAssignedActiveTrainersToTrainee(username);
      response = ResponseEntity.status(HttpStatus.OK).body(list);
    } catch (UserNotFoundException e) {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Then("the expected status response code {int}")
  public void thenResponseContainsErrorMessage(int expected) {
    assertEquals("Expected status code", expected, response.getStatusCodeValue());
  }
}
