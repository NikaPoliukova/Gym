package cucumberComponentTest.steps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class DeleteTraineeStepDefinitions {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private Response response;
  private String username;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainees";
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
    jwtUtils.addTokenToCookie(token);
  }

  @When("the user send a DELETE request to delete the trainee")
  public void whenUserSendDeleteRequest() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .formParam(USERNAME, username)
        .delete(BASE_URI);
  }

  @Then("we should to get response with status code {int}")
  public void weShouldToGetResponseWithStatusCode(int expectedStatus) {
    assertEquals("Expected status code", expectedStatus, response.getStatusCode());
  }
}
