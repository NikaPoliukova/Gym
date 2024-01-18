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
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class FindNotActiveTrainersStepDefinitions {
  private Response response;
  private String username;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;
  private String baseUri = "http://localhost:8091/api/v1/trainees";

  @Given("the user enter username for get not active trainers for trainee")
  public void theUserEnterUsernameForGetNotActiveTrainersForTrainee(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = traineeDataList.get(0);
    username = userData.get("username");
  }

  @And("prepare token for request for get not active trainers for trainee")
  public void prepareTokenForRequestForGetNotActiveTrainersForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("send a GET request to find not active trainers for trainee")
  public void sendAGETRequestToFindNotActiveTrainersForTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .get(baseUri + "/not-active-trainers");
  }

  @Then("the response contains list of trainers")
  public void theResponseContainsListOfTrainers() {
    assertNotNull("Expected list of trainers", response.getBody());
  }

  @And("should be response status code {int}")
  public void shouldBeResponseStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
