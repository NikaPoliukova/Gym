//package cucumberIntegrationTest.steps;
//
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import upskill.dto.UpdateTraineeTrainerDto;
//import upskill.entity.Trainer;
//import upskill.service.TrainingService;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class UpdateTrainerListSteps {
//
//  private ResponseEntity<Object> response;
//  private UpdateTraineeTrainerDto dto;
//  @Autowired
//  private TrainingService trainingService;
//  private List<Trainer> list;
//
//
//  @Given("the params for update trainer list for trainee")
//  public void provideValidUsername() {
//    dto = new UpdateTraineeTrainerDto("Trainee.Trainee", LocalDate.of(2023,12,13),
//        "YOGA", list);
//  }
//
//  @When("the user makes a PUT request for update list")
//  public void makePutRequest() {
//    // Implement the logic to make a PUT request to the specified endpoint
//  }
//
//  @Then("the response should contain a list of TrainerDtoForTrainee")
//  public void verifyStatusCode() {
//    // Implement the logic to verify the response status code is 200
//  }
//
//  @And("the response status code should be 200")
//  public void verifyResponseContainsTrainerDtoList() {
//    // Implement the logic to verify that the response contains a list of TrainerDtoForTrainee
//  }
//
//}
