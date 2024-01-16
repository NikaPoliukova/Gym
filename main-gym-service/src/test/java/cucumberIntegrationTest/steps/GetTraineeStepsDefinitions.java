package cucumberIntegrationTest.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.exception.UserNotFoundException;
import upskill.service.TraineeService;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class GetTraineeStepsDefinitions {
  @Autowired
  private TraineeService traineeService;
  private String username;
  private ResponseEntity<Object> response;

  @Given("user with username")
  public void givenUserWithUsername() {
   username = "PAVEL.TRAINEE";
  }
  @Given("user with incorrect username")
  public void givenUserWithInvalidUsername() {
    username = "Incorrect.Name";
  }

  @When("the user sends a GET request to obtain information about the trainee")
  public void whenUserRequestsTraineeData() {
    try {
      var trainee = traineeService.findByUsername(username);
      response = ResponseEntity.status(HttpStatus.OK).body(trainee);
    } catch (UserNotFoundException e) {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Then("the response contains information about the trainee")
  public void thenResponseContainsTraineeData() {
    assertNotNull("Expected Trainee", response.getBody());
  }

  @And("we get the status response code {int}")
  public void andResponseStatusIs(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCodeValue());
  }
}
