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
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;
import upskill.exception.OperationFailedException;
import upskill.service.config.TestMongoConfig;

import java.util.ArrayList;
import java.util.List;

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
    TrainingTrainerSummary training =  service.findByUsername(USERNAME)
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
}




