package upskill.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import upskill.dao.TrainerTrainingRepositoryCustom;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;
import upskill.exception.OperationFailedException;
import upskill.service.config.TestMongoConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
@ContextConfiguration(classes = TestMongoConfig.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureDataMongo
public class TrainingSummaryServiceIntegrationTest {
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
  private MongoTemplate template;
  @Autowired
  private TrainerTrainingRepositoryCustom repository;

  @Test
  void saveTraining_ShouldBeSavedSuccessfully() {
    // Arrange
    var dto = TRAINING_TRAINER_SUMMARY;
    // Act
    var savedTraining = service.saveTraining(dto);
    // Assert
    assertNotNull(savedTraining.getId());
    assertEquals(dto.getUsername(), savedTraining.getUsername());
    //rollback
    template.remove(dto);
  }

  @Test
  void saveTraining_ShouldThrowExceptionOnFailure() {
    // Arrange
    service.saveTraining(TRAINING_TRAINER_SUMMARY);
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
    var dto = new TrainingSummaryService.UpdateParamsDto(TRAINING_TRAINER_SUMMARY,
        NEW_YEAR, MONTH, DURATION);
    // Act
    service.createNewYear(dto);
    // Assert
    var updatedTraining = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));
    assertEquals(1, updatedTraining.getYearsList().size());
    YearData newYear = updatedTraining.getYearsList().get(0);
    assertEquals(NEW_YEAR, newYear.getYear());
    assertEquals(1, newYear.getMonthsList().size());
    var newMonth = newYear.getMonthsList().get(0);
    assertEquals(MONTH, newMonth.getMonthValue());
    assertEquals(DURATION, newMonth.getTrainingsSummaryDuration());
    //rollback
    repository.deleteYear(updatedTraining.getId(), newYear.getYear());
  }

  @Test
  void createNewMonth_ShouldAddNewMonth() {
    // Arrange
    var date = new YearData(NEW_YEAR, new ArrayList<>());
    repository.createYear(TRAINING_TRAINER_SUMMARY.getUsername(), date);
    var training = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));
    var existingYear = training.getYearsList().get(0);
    var dto = new TrainingSummaryService.DtoForCreateNewMonth(training, existingYear, NEW_MONTH, DURATION);
    // Act
    service.createNewMonth(dto);
    // Assert
    var updatedTraining = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));

    assertEquals(1, updatedTraining.getYearsList().size());
    var updatedYear = updatedTraining.getYearsList().get(0);

    assertEquals(1, updatedYear.getMonthsList().size());
    MonthData newMonth = updatedYear.getMonthsList().get(0);
    assertEquals(NEW_MONTH, newMonth.getMonthValue());
    assertEquals(DURATION, newMonth.getTrainingsSummaryDuration());

    //rollback
    repository.deleteMonth(training.getId(), date.getYear(), NEW_MONTH);
    repository.deleteYear(training.getId(), date.getYear());
  }

  @Test
  void updateMonthDuration_ShouldAddDuration() {
    // Arrange
    var existingYear = new YearData(NEW_YEAR, List.of(new MonthData(NEW_MONTH, DURATION)));
    repository.createYear(TRAINING_TRAINER_SUMMARY.getUsername(), existingYear);
    var oldTraining = service.findByUsername(USERNAME)
        .orElseThrow(() -> new RuntimeException("Training not found"));
    var month = existingYear.getMonthsList().get(0);
    var newDuration = month.getTrainingsSummaryDuration() + DURATION;
    var dto = new TrainingSummaryService.UpdateParamsDto(oldTraining, existingYear.getYear(),
        month.getMonthValue(), newDuration);
    // Act
    service.updateDuration(dto);
    // Assert
    var updatedTraining = service.findByUsername(USERNAME).get();
    var updatedYear = updatedTraining.getYearsList().get(0);
    assertEquals(existingYear.getMonthsList().size(), updatedYear.getMonthsList().size());
    MonthData newMonth = updatedYear.getMonthsList().get(0);
    assertEquals(newDuration, newMonth.getTrainingsSummaryDuration());
    //rollback
    repository.deleteMonth(updatedTraining.getId(), existingYear.getYear(), NEW_MONTH);
    repository.deleteYear(updatedTraining.getId(), existingYear.getYear());
  }

  @Test
  void deleteTraining_ShouldDeleteMonthData() {
    // Arrange
    var existingYear = new YearData(NEW_YEAR, List.of(new MonthData(NEW_MONTH, DURATION)));
    repository.createYear(TRAINING_TRAINER_SUMMARY.getUsername(), existingYear);
    var training = service.getTrainingTrainerSummary(USERNAME);
    var date = LocalDate.of(NEW_YEAR, NEW_MONTH, 1);
    var dto = new TrainerWorkloadRequestForDelete(training.get().getUsername(), date, 1);
    // Act
    service.deleteTraining(dto);
    // Assert
    var updatedTraining = service.getTrainingTrainerSummary(dto.getTrainerUsername());
    assertEquals(0, updatedTraining.get().getYearsList().size(), "Training should be deleted");

    //rollback
    repository.createNewMonth(updatedTraining.get().getUsername(), existingYear);
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

  @Test
  void testGetTrainerWorkload() {
    // Arrange
    var existingYear = new YearData(NEW_YEAR, List.of(new MonthData(NEW_MONTH, DURATION)));
    repository.createYear(TRAINING_TRAINER_SUMMARY.getUsername(), existingYear);
    var training = service.findByUsername(USERNAME).get();
    var yearDate = training.getYearsList().get(0);
    var month = yearDate.getMonthsList().get(0);
    // Act
    var trainingSummary = service.getTrainerWorkload(training.getUsername(),
        yearDate.getYear(), month.getMonthValue());
    // Assert
    assertEquals(month.getTrainingsSummaryDuration(), trainingSummary);
  }
}




