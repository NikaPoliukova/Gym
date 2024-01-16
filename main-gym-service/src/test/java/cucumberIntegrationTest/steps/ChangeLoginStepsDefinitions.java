package cucumberIntegrationTest.steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upskill.dto.UserUpdatePass;
import upskill.exception.UserNotFoundException;
import upskill.service.UserService;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class ChangeLoginStepsDefinitions {
  private String username;
  private String oldPassword;
  private String newPassword;
  private ResponseEntity<Object> response;

  @Autowired
  private UserService userService;

  @When("the user enter correct credentials for update password")
  public void enterCorrectCredentials() {
    username = "VADIK.TRAINEE";
    oldPassword = "5jwwMXUv6k";
    newPassword = "1234567891";
  }

  @When("the user enter incorrect credentials for update password")
  public void enterIncorrectCredentials() {
    username = "incorrect";
    oldPassword = "EqT92opQcH";
    newPassword = "1234567891";
  }

  @When("user click update password button")
  public void updatePassword() {
    try {
      userService.updatePassword(new UserUpdatePass(username, oldPassword, newPassword));
      response = ResponseEntity.status(HttpStatus.OK).build();
    } catch (UserNotFoundException e) {
      response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @Then("the user is successfully change password")
  public void verifyLoginResponse() {
    assertEquals("Expected status code", 200,
        response.getStatusCodeValue());
  }

  @Then("the user failed updating password")
  public void verifyLoginResponseIfIncorrectData() {
    assertEquals("Expected status code", 400, response.getStatusCodeValue());
  }
}
