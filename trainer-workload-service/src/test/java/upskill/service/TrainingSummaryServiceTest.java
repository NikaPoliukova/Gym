package upskill.service;

import com.mongodb.BasicDBObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import upskill.dao.TrainerTrainingRepository;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainerWorkloadRequestForDelete;
import upskill.entity.MonthData;
import upskill.entity.TrainingTrainerSummary;
import upskill.entity.YearData;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class TrainingSummaryServiceTest {

  public static final String TEST_TRAINER = "testTrainer";
  public static final int DURATION = 30;
  public static final int MONTH = 1;
  public static final int YEAR = 2022;
  public static final String USERNAME = "username";
  @Mock
  private MongoTemplate mongoTemplate;

  @Mock
  private TrainerTrainingRepository trainingRepository;

  @InjectMocks
  private TrainingSummaryService trainingSummaryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testDeleteTraining() {
    // Arrange
    var dto = getTrainerWorkloadRequestForDelete();
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    // Act
    trainingSummaryService.deleteTraining(dto);
    // Assert
    assertEquals(0, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).save(any());
  }

  @Test
  void testSaveTraining() {
    // Arrange
    var dto = getTrainerTrainingDtoForSave();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(null);
    // Act
    trainingSummaryService.saveTraining(dto);
    // Assert
    verify(trainingRepository, times(1)).save(any());
  }

  @Test
  void testUpdateExistingMonth() {
    // Arrange
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    // Act
    trainingSummaryService.updateExistingMonth(trainer, trainer.getYearsList().get(0),
        new MonthData(MONTH, 150),
        DURATION);
    // Assert
    assertEquals(30, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
  }

  @Test
  void testUpdateExistingYear_WhenYearExists() {
    // Arrange
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    // Act
    trainingSummaryService.updateExistingYear(trainer, trainer.getYearsList().get(0), MONTH, DURATION);
    // Assert
    assertEquals(60, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
  }

  @Test
  void testUpdateExistingYear_WhenYearDoesNotExist() {
    // Arrange
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    // Act
    trainingSummaryService.updateExistingYear(trainer, getYear(), MONTH, DURATION);
    // Assert
    assertEquals(1, trainer.getYearsList().size());
    assertEquals(30, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
  }

  @Test
  void testDeleteYear() {
    // Arrange
    var trainer = getTrainingTrainerSummary();
    // Act
    trainingSummaryService.deleteYear(trainer, getYear());
    // Assert
    var expectedQuery = Query.query(Criteria.where(USERNAME).is(TEST_TRAINER).and("yearsList.year")
        .is(YEAR));
    var expectedUpdate = new Update().pull("yearsList", new BasicDBObject("year", YEAR));
    verify(mongoTemplate, times(1)).updateFirst(expectedQuery, expectedUpdate,
        TrainingTrainerSummary.class);
  }

  @Test
  void testDeleteMonth() {
    // Arrange
    var trainer = getTrainingTrainerSummary();
    var monthData = new MonthData(MONTH, DURATION);
    // Act
    trainingSummaryService.deleteMonth(trainer, getYear(), monthData);
    // Assert
    var expectedQuery = Query.query(Criteria.where(USERNAME).is(TEST_TRAINER)
        .and("yearsList.year").is(YEAR));
    var expectedUpdate = new Update().pull("yearsList.$.monthsList", new BasicDBObject("monthValue", MONTH));
    verify(mongoTemplate, times(1)).updateFirst(expectedQuery, expectedUpdate,
        TrainingTrainerSummary.class);
  }


  private static TrainerTrainingDtoForSave getTrainerTrainingDtoForSave() {
    var dto = new TrainerTrainingDtoForSave();
    dto.setTrainerUsername(TEST_TRAINER);
    dto.setTrainingDate(prepareDate());
    dto.setDuration(DURATION);
    return dto;
  }

  private static YearData getYear() {
    return new YearData(YEAR, Collections.singletonList(new MonthData(MONTH,
        DURATION)));
  }

  private static LocalDate prepareDate() {
    return LocalDate.of(YEAR, MONTH, 1);
  }

  private static TrainingTrainerSummary getTrainingTrainerSummary() {
    var trainer = new TrainingTrainerSummary();
    trainer.setUsername(TEST_TRAINER);
    trainer.setYearsList(Collections.singletonList(getYear()));
    return trainer;
  }


  private static TrainerWorkloadRequestForDelete getTrainerWorkloadRequestForDelete() {
    var dto = new TrainerWorkloadRequestForDelete();
    dto.setTrainerUsername(TEST_TRAINER);
    dto.setTrainingDate(prepareDate());
    dto.setDuration(DURATION);
    return dto;
  }
}
