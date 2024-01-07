package upskill.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import upskill.dao.TrainerTrainingRepository;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;
import upskill.exception.OperationFailedException;
import upskill.service.config.TestMongoConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@TestPropertySource(locations = "classpath:application-test.yml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TrainingSummaryServiceIntegrationTest {
  public static final String USERNAME = "Nika.Trainer";
  public static final TrainingTrainerSummary TRAINING_TRAINER_SUMMARY =
      new TrainingTrainerSummary("Nika.Trainer", "Nika", "Trainer",
          true, new ArrayList<>());
  public static final int DURATION = 60;
  public static final int MONTH = 1;
  public static final int NEW_YEAR = 2030;
  public static final int NEW_MONTH = 3;

  @Autowired
  private TrainingSummaryService service;
  @Autowired
  private TrainerTrainingRepository trainingRepository;
  @MockBean
  private RabbitTemplate rabbitTemplate;


  @Test
  void saveTraining_ShouldPersistData() {
    // Arrange
    TrainingTrainerSummary dto = TRAINING_TRAINER_SUMMARY;
    // Act
    TrainingTrainerSummary savedTraining = service.saveTraining(dto);
    // Assert
    assertNotNull(savedTraining.getId());
    assertEquals(dto.getUsername(), savedTraining.getUsername());
  }

  @Test
  void saveTraining_ShouldThrowExceptionOnFailure() {
    // Act & Assert
    assertThrows(OperationFailedException.class, () -> service.saveTraining(TRAINING_TRAINER_SUMMARY));
  }

  @Test
  void findByUsername_ShouldReturnEmptyOptionalIfNotFound() {
    // Act
    var notFoundTraining = service.findByUsername("nonexistentUsername");
    // Assert
    assertTrue(notFoundTraining.isEmpty());
  }

  @Test
  void findByUsername_ShouldReturnTrainingTrainerSummary() {
    // Act
    var foundTraining = service.findByUsername(USERNAME);
    // Assert
    assertTrue(foundTraining.isPresent());
    assertEquals(USERNAME, foundTraining.get().getUsername());
  }


  @Test
  void createNewYear_ShouldAddNewYear() {
    // Arrange
    TrainingSummaryService.UpdateParamsDto dto = new TrainingSummaryService.UpdateParamsDto(TRAINING_TRAINER_SUMMARY,
        NEW_YEAR, MONTH, DURATION);
    // Act
    service.createNewYear(dto);
    // Assert
    TrainingTrainerSummary updatedTraining = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));
    assertEquals(1, updatedTraining.getYearsList().size());
    YearData newYear = updatedTraining.getYearsList().get(0);
    assertEquals(NEW_YEAR, newYear.getYear());
    assertEquals(1, newYear.getMonthsList().size());
    MonthData newMonth = newYear.getMonthsList().get(0);
    assertEquals(MONTH, newMonth.getMonthValue());
    assertEquals(DURATION, newMonth.getTrainingsSummaryDuration());
  }

  @Test
  void createNewMonth_ShouldAddNewMonth() {
    // Arrange
    TrainingTrainerSummary training = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));
    YearData existingYear = training.getYearsList().get(0);
    TrainingSummaryService.DtoForCreateNewMonth dto =
        new TrainingSummaryService.DtoForCreateNewMonth(training, existingYear, NEW_MONTH, DURATION);
    // Act
    service.createNewMonth(dto);
    // Assert
    TrainingTrainerSummary updatedTraining = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));

    assertEquals(1, updatedTraining.getYearsList().size());
    YearData updatedYear = updatedTraining.getYearsList().get(0);

    assertEquals(2, updatedYear.getMonthsList().size());
    MonthData newMonth = updatedYear.getMonthsList().get(1);
    assertEquals(NEW_MONTH, newMonth.getMonthValue());
    assertEquals(DURATION, newMonth.getTrainingsSummaryDuration());
  }

  @Test
  void deleteTraining_ShouldDeleteYearAndMonthData() {
    // Arrange
    var training = service.getTrainingTrainerSummary(USERNAME);
    var yearDate = training.get().getYearsList().get(0);
    var monthDate = yearDate.getMonthsList().get(0);
    var date = LocalDate.of(yearDate.getYear(), monthDate.getMonthValue(), 1);
    var dto = new TrainerWorkloadRequestForDelete(training.get().getUsername(), date, 1);
    // Act
    service.deleteTraining(dto);
    // Assert
    var updatedTraining = service.getTrainingTrainerSummary(dto.getTrainerUsername());
    assertEquals(0, updatedTraining.get().getYearsList().size(), "Training should be deleted");
  }
//
//  @Test
//  void deleteTraining_ShouldUpdateMonthDataDuration() {
//    // Arrange
//    TrainerWorkloadRequestForDelete dto = prepareTestData();
//    int originalDuration = dto.getDuration();
//    trainingSummaryService.createNewMonth(prepareCreateNewMonthDto(dto), originalDuration);
//
//    // Act
//    trainingSummaryService.deleteTraining(dto);
//
//    // Assert
//    Optional<TrainingTrainerSummary> updatedTraining = trainingSummaryService.getTrainingTrainerSummary(dto.getTrainerUsername());
//    assertTrue(updatedTraining.isPresent(), "Training should exist");
//    List<YearData> yearsList = updatedTraining.get().getYearsList();
//    assertEquals(1, yearsList.size(), "There should be one year");
//    List<MonthData> monthsList = yearsList.get(0).getMonthsList();
//    assertEquals(1, monthsList.size(), "There should be one month");
//    MonthData updatedMonth = monthsList.get(0);
//    assertEquals(originalDuration - dto.getDuration(), updatedMonth.getTrainingsSummaryDuration(),
//        "MonthData duration should be updated");
//  }
//
//  @Test
//  void deleteTraining_ShouldNotDeleteYearDataIfOtherMonthsExist() {
//    // Arrange
//    TrainerWorkloadRequestForDelete dto = prepareTestData();
//    trainingSummaryService.createNewMonth(prepareCreateNewMonthDto(dto), dto.getDuration() - 1);
//
//    // Act
//    trainingSummaryService.deleteTraining(dto);
//
//    // Assert
//    Optional<TrainingTrainerSummary> updatedTraining = trainingSummaryService.getTrainingTrainerSummary(dto.getTrainerUsername());
//    assertTrue(updatedTraining.isPresent(), "Training should exist");
//    List<YearData> yearsList = updatedTraining.get().getYearsList();
//    assertEquals(1, yearsList.size(), "There should be one year");
//    List<MonthData> monthsList = yearsList.get(0).getMonthsList();
//    assertEquals(1, monthsList.size(), "There should be one month");
//  }

}




