package upskill.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    var dto = getTrainerWorkloadRequestForDelete();
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    trainingSummaryService.deleteTraining(dto);
    assertEquals(0, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).save(any());
  }

  @Test
  void testSaveTraining() {
    var dto = getTrainerTrainingDtoForSave();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(null);
    trainingSummaryService.saveTraining(dto);
    verify(trainingRepository, times(1)).save(any());
  }


  @Test
  void testUpdateExistingMonth() {
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    trainingSummaryService.updateExistingMonth(trainer, trainer.getYearsList().get(0),
        new MonthData(MONTH, 150),
        DURATION);
    assertEquals(30, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
  }

  @Test
  void testUpdateExistingYear_WhenYearExists() {
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    trainingSummaryService.updateExistingYear(trainer, trainer.getYearsList().get(0), MONTH, DURATION);
    assertEquals(60, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
  }

  @Test
  void testUpdateExistingYear_WhenYearDoesNotExist() {
    var trainer = getTrainingTrainerSummary();
    when(mongoTemplate.findOne(any(Query.class), eq(TrainingTrainerSummary.class)))
        .thenReturn(trainer);
    trainingSummaryService.updateExistingYear(trainer, getYear(), MONTH, DURATION);
    assertEquals(1, trainer.getYearsList().size());
    assertEquals(30, trainer.getYearsList().get(0).getMonthsList().get(0).getTrainingsSummaryDuration());
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class),
        eq(TrainingTrainerSummary.class));
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
