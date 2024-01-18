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
import upskill.dto.TraineeResponse;
import upskill.security.JwtUtils;

import javax.servlet.http.Cookie;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class GetTraineeStepsDefinitions {
  private String username;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Cookie cookie;
  private Response response;
  private String baseUri = "http://localhost:8091/api/v1/trainees";

  @Given("the user enter username for get trainee")
  public void theUserEnterUsernameForGetTrainee(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = traineeDataList.get(0);
    username = userData.get("username");
  }

  @And("prepare token for request for trainee")
  public void prepareTokenForRequestForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    var authentication = new UsernamePasswordAuthenticationToken(username, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    cookie = new Cookie("Bearer", token);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge((int) Duration.ofHours(10).toSeconds());
  }

  @When("The user send a GET request to get information about the trainee")
  public void theUserSendAGETRequestToGetInformationAboutTheTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC)
        .cookie("Bearer", token)
        .header("Authorization", "Bearer " + token)
        .formParam("username", username)
        .get(baseUri + "/trainee");
  }
@Then("the response contains information about the trainee")
  public void theResponseContainsInformationAboutTheTrainee() {
    assertThat(response.getBody().as(TraineeResponse.class)).isNotNull();
  }

  @Then("response status code should be {int}")
  public void responseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }
}
