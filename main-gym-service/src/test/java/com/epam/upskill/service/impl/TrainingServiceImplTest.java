package com.epam.upskill.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.converter.TrainingConverter;
import upskill.dao.TrainingRepository;
import upskill.dto.*;
import upskill.entity.Trainee;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.entity.TrainingType;
import upskill.exception.OperationFailedException;
import upskill.service.TraineeService;
import upskill.service.TrainerService;
import upskill.service.impl.TrainingServiceImpl;
import upskill.service.messageService.SenderMessagesForSaveService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TrainingServiceImplTest {

  public static final LocalDate PERIOD_FROM = LocalDate.parse("2023-01-01");
  public static final LocalDate PERIOD_TO = LocalDate.parse("2023-11-01");
  public static final String TRAINEE_USERNAME = "trainee_username";
  public static final String TRAINER_USERNAME = "trainer_username";
  public static final String TRAINING_NAME = "training_name";
  public static final String TYPE = "YOGA";
  public static final int DURATION = 60;
  public static final long TRAINING_ID = 1L;

  @InjectMocks
  private TrainingServiceImpl trainingService;

  @Mock
  private TrainingRepository trainingRepository;

  @Mock
  private TraineeService traineeService;

  @Mock
  private TrainerService trainerService;

  @Mock
  private TrainingConverter trainingConverter;

  @Mock
  private SenderMessagesForSaveService messagesForSaveService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindTrainingById() {
    long trainingId = 1L;
    when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(getTraining()));
    var result = trainingService.findTrainingById(trainingId);
    assertNotNull(result);
    assertEquals(getTraining(), result);
  }

  @Test
  void testSaveTraining_Successful() {
    // Arrange
    var request = getTrainingRequest();
    when(traineeService.findByUsername(request.traineeUsername())).thenReturn(getTrainee());
    when(trainerService.findByUsername(request.trainerUsername())).thenReturn(getTrainer());
    when(trainingRepository.findTrainingTypeByName(request.trainingType())).thenReturn(getTrainingType());
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenAnswer(invocation -> {
      TrainingRequest req = invocation.getArgument(0);
      Training trainingObj = invocation.getArgument(1);
      trainingObj.setTrainee(traineeService.findByUsername(req.traineeUsername()));
      trainingObj.setTrainer(trainerService.findByUsername(req.trainerUsername()));
      return trainingObj;
    });
    when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenReturn(getTraining());

    // Act
    var result = trainingService.saveTraining(request);

    // Assert
    assertNotNull(result);
    verify(messagesForSaveService, times(1)).sendJsonMessage(any());
  }


  @Test
  @Transactional
  void testSaveTraining_Failed() {
    // Arrange
    var request = getTrainingRequest();
    when(traineeService.findByUsername(request.traineeUsername())).thenReturn(getTrainee());
    when(trainerService.findByUsername(request.trainerUsername())).thenReturn(getTrainer());
    when(trainingRepository.findTrainingTypeByName(request.trainingType())).thenReturn(getTrainingType());
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenAnswer(invocation -> {
      TrainingRequest req = invocation.getArgument(0);
      Training trainingObj = invocation.getArgument(1);
      trainingObj.setTrainee(traineeService.findByUsername(req.traineeUsername()));
      trainingObj.setTrainer(trainerService.findByUsername(req.trainerUsername()));
      return trainingObj;
    });
    when(trainingRepository.save(ArgumentMatchers.any(Training.class))).thenThrow(new RuntimeException("Some error"));

    // Act and Assert
    assertThrows(OperationFailedException.class, () -> trainingService.saveTraining(request));
    verify(messagesForSaveService, never()).sendJsonMessage(any());
  }

  private static TrainingRequest getTrainingRequest() {
    return new TrainingRequest(TRAINEE_USERNAME, TRAINER_USERNAME, TYPE, LocalDate.now(),
        TYPE, DURATION);
  }


  @Test
  void testFindTrainingsByUsernameAndCriteria() {
    var trainingType = TrainingTypeEnum.STRENGTH;
    var expectedTrainings = Collections.singletonList(new Training());
    when(trainingRepository.findTraineeTrainingsList(any(TrainingDtoRequest.class))).thenReturn(expectedTrainings);
    var request = new TrainingTraineeRequest(TRAINEE_USERNAME, PERIOD_FROM, PERIOD_TO, TRAINING_NAME,
        trainingType.name());
    var result = trainingService.findTrainingsByUsernameAndCriteria(request);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(expectedTrainings, result);
  }

  @Test
  void testFindTrainerTrainings() {
    var expectedTrainings = Collections.singletonList(new Training());
    when(trainerService.findByUsername(TRAINER_USERNAME)).thenReturn(new Trainer());
    when(trainingRepository.findTrainerTrainings(any(TrainingTrainerDto.class))).thenReturn(expectedTrainings);
    var request = new TrainingTrainerRequest(TRAINER_USERNAME, PERIOD_FROM, PERIOD_TO, TRAINEE_USERNAME);
    var result = trainingService.findTrainerTrainings(request);
    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(expectedTrainings, result);
  }

  @Test
  void testFindNotAssignedActiveTrainersToTrainee() {
    Trainee trainee = getTrainee();
    trainee.setId(1L);
    Trainer trainer = getTrainer();
    trainer.setId(2L);
    when(traineeService.findByUsername(TRAINEE_USERNAME)).thenReturn(trainee);
    var activeTrainers = Collections.singletonList(trainer);
    when(trainingRepository.getAssignedActiveTrainersToTrainee(trainee.getId())).thenReturn(activeTrainers);
    var trainersList = Collections.singletonList(new Trainer());
    when(trainerService.findAll()).thenReturn(trainersList);
    var result = trainingService.findNotAssignedActiveTrainersToTrainee(TRAINEE_USERNAME);
    assertNotNull(result);
    assertTrue(true);
  }

  @Test
  void testDelete() {
    var training = getTraining();
    doNothing().when(trainingRepository).delete(training);
    assertDoesNotThrow(() -> trainingService.delete(training));
  }

  @Test
  void testUpdateTraineeTrainerList() {
    var dto = new UpdateTraineeTrainerDto(TRAINEE_USERNAME, LocalDate.now().toString(), TRAINING_NAME,
        Collections.emptyList());
    var trainee = getTrainee();
    trainee.setUsername(TRAINEE_USERNAME);
    var training = getTraining();
    training.setTrainingName(TRAINING_NAME);
    when(traineeService.findByUsername(TRAINEE_USERNAME)).thenReturn(trainee);
    when(trainingRepository.findTraineeTrainingsList(trainee.getId(), LocalDate.now().toString(),
        TRAINING_NAME)).thenReturn(Collections.singletonList(training));
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenReturn(new Training());
    when(trainingRepository.save(any(Training.class))).thenReturn(new Training());
    var result = trainingService.updateTraineeTrainerList(dto);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindTrainingById_WhenTrainingNotFound() {
    when(trainingRepository.findById(TRAINING_ID)).thenReturn(Optional.empty());
    assertThrows(OperationFailedException.class, () -> {
      trainingService.findTrainingById(TRAINING_ID);
    });
  }


  @Test
  void testSaveTraining_WhenTraineeNotFound() {
    var request = new TrainingRequest(TRAINEE_USERNAME, TRAINER_USERNAME, TRAINING_NAME,
        PERIOD_FROM, TRAINING_NAME, DURATION);
    when(traineeService.findByUsername(request.traineeUsername())).thenReturn(null);
    when(trainerService.findByUsername(request.trainerUsername())).thenReturn(new Trainer());
    when(trainingRepository.findTrainingTypeByName(request.trainingType())).thenReturn(new TrainingType());
    assertThrows(NullPointerException.class, () -> {
      trainingService.saveTraining(request);
    });
  }

  private static Trainee getTrainee() {
    Trainee trainee = new Trainee();
    return trainee;
  }

  private static Training getTraining() {
    Training training = new Training();
    return training;
  }

  private static TrainingType getTrainingType() {
    TrainingType trainingType = new TrainingType();
    return trainingType;
  }

  private static Trainer getTrainer() {
    Trainer trainer = new Trainer();
    return trainer;
  }
}
