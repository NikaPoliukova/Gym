package cucumberIntegrationTest.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.exception.UserNotFoundException;
import upskill.service.UserService;

import static org.springframework.test.util.AssertionErrors.assertEquals;


public class LoginStepDefinitions {
  @Autowired
  private UserService service;

  private String name;
  private String pass;
  private ResponseEntity<Object> response;

  @When("the user enters their correct credentials")
  public void enterCredentials() {
    name = "PAVEL.TRAINEE";

  }

  @When("user click login button")
  public void performLogin() {
    var user = service.findByUsername(name);
    if (user != null) {
      response = ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
  }

  @When("user click login button with invalid data")
  public void performLoginWithInvalidData() {
    try {
     service.findByUsername(name);
    } catch (UserNotFoundException e) {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(UserNotFoundException.class);
    }
  }

  @When("the user enters their incorrect credentials")
  public void enterIncorrectCredentials() {
    name = "incorrect";
    pass = "EzKcRxqLrb";
  }

  @Then("the user is successfully authenticated")
  public void verifyLoginResponse() {
    assertEquals("Expected status code", 201, response.getStatusCodeValue());
  }

  @Then("the user failed authenticate")
  public void verifyLoginResponseIfIncorrectData() {
    assertEquals("User  'incorrect' was not found", 400, response.getStatusCodeValue());
  }
}
