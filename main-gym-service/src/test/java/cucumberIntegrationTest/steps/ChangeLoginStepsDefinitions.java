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
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class ChangeLoginStepsDefinitions {
  private String username;
  private String oldPassword;
  private String newPassword;
  private String baseUri = "http://localhost:8091/api/v1/users/user";
  private Response response;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  @Given("the user enter params for update password")
  public void theUserEnterParamsForUpdatePassword(DataTable dataTable) {
    List<Map<String, String>> userDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = userDataList.get(0);
    username = userData.get("username");
    oldPassword = userData.get("oldPassword");
    newPassword = userData.get("newPassword");
  }

  @And("prepare token for request")
  public void prepareTokenForRequest() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("The user send a PUT request to update")
  public void theUserSendAPUTRequestToUpdate() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .formParam("oldPassword", oldPassword)
        .formParam("newPassword", newPassword)
        .put(baseUri + "/setting/login");
  }

  @Then("The response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }
}

