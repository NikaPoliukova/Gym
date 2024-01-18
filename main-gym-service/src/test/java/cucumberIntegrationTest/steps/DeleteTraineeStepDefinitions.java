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

public class DeleteTraineeStepDefinitions {
  private Response response;
  private String username;
  private String baseUri = "http://localhost:8091/api/v1/trainees";
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;

  @Given("the user enter username for delete trainee")
  public void theUserEnterUsernameForDeleteTrainee(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = traineeDataList.get(0);
    username = userData.get("username");
  }

  @And("prepare token for request for delete trainee")
  public void prepareTokenForRequestForDeleteTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("the user send a DELETE request to delete the trainee")
  public void whenUserSendDeleteRequest() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .delete(baseUri);
  }

  @Then("we get status response code after delete {int}")
  public void thenResponseHasStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }


  @Then("we should to get response with status code {int}")
  public void weShouldToGetResponseWithStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
