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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.util.AssertionErrors.assertEquals;

//@ActiveProfiles("test")
//@TestPropertySource(properties = "spring.config.activate.on-profile=test")
public class GetTraineeStepsDefinitions {
  private static final String USERNAME = "username";
  private static final String AUTHORIZATION = "Authorization";
  private String username;
  @Autowired
  private JwtUtils jwtUtils;
  private String token;
  private Response response;
  private static final String BASE_URI = "http://localhost:8091/api/v1/trainees";

  @Given("the user enter username for get trainee")
  public void theUserEnterUsernameForGetTrainee(DataTable dataTable) {
    List<Map<String, String>> traineeDataList = dataTable.asMaps(String.class, String.class);
    Map<String, String> userData = traineeDataList.get(0);
    username = userData.get(USERNAME);
  }

  @And("prepare token for request for trainee")
  public void prepareTokenForRequestForTrainee() {
    token = jwtUtils.generateAccessTokenForTest(username);
    jwtUtils.addTokenToCookie(token);
  }


  @When("The user send a GET request to get information about the trainee")
  public void theUserSendAGETRequestToGetInformationAboutTheTrainee() {
    response = RestAssured
        .given()
        .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .cookie("Bearer", token)
        .header(AUTHORIZATION, "Bearer " + token)
        .formParam(USERNAME, username)
        .get(BASE_URI + "/trainee");
  }

  @Then("the response contains information about the trainee")
  public void theResponseContainsInformationAboutTheTrainee() {
    assertThat(response.getBody()).isNotNull();
  }

  @Then("response status code should be {int}")
  public void responseStatusCodeShouldBe(int expectedStatus) {
    assertEquals("expected status", expectedStatus, response.getStatusCode());
  }
}
