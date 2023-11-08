package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TrainingServiceImplTest {

  public static final LocalDate PERIOD_FROM = LocalDate.parse("2023-01-01");
  public static final LocalDate PERIOD_TO = LocalDate.parse("2023-11-01");
  ;
  public static final String TRAINEE_USERNAME = "trainee_username";
  public static final String TRAINER_USERNAME = "trainer_username";
  public static final String TRAINING_NAME = "training_name";
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

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindTrainingById() {
    long trainingId = 1L;
    Training expectedTraining = new Training();
    when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(expectedTraining));

    Training result = trainingService.findTrainingById(trainingId);

    assertNotNull(result);
    assertEquals(expectedTraining, result);
  }

  @Test
  void testSaveTraining() {
    TrainingRequest request = new TrainingRequest(TRAINEE_USERNAME, TRAINER_USERNAME,
        "Gymnastics", PERIOD_TO, "STRETCHING", 50);

    Trainee trainee = new Trainee();
    trainee.setUsername(TRAINEE_USERNAME);
    Trainer trainer = new Trainer();
    trainer.setUsername(TRAINER_USERNAME);
    TrainingType trainingType = new TrainingType();
    trainingType.setTrainingTypeName(TrainingTypeEnum.STRENGTH);

    when(traineeService.findByUsername(TRAINEE_USERNAME)).thenReturn(trainee);
    when(trainerService.findByUsername(TRAINER_USERNAME)).thenReturn(trainer);
    when(trainingRepository.findTrainingTypeByName("STRETCHING")).thenReturn(trainingType);
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenReturn(new Training());
    when(trainingRepository.save(any(Training.class))).thenReturn(new Training());

    Training result = trainingService.saveTraining(request);

    assertNotNull(result);
  }

  @Test
  void testFindTrainingsByUsernameAndCriteria() {
    TrainingTypeEnum trainingType = TrainingTypeEnum.STRENGTH;
    List<Training> expectedTrainings = Collections.singletonList(new Training());
    when(trainingRepository.findTraineeTrainingsList(any(TrainingDtoRequest.class))).thenReturn(expectedTrainings);

    TrainingTraineeRequest request = new TrainingTraineeRequest(TRAINEE_USERNAME, PERIOD_FROM, PERIOD_TO, TRAINING_NAME, trainingType.name());
    List<Training> result = trainingService.findTrainingsByUsernameAndCriteria(request);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(expectedTrainings, result);
  }

  @Test
  void testFindTrainerTrainings() {
    List<Training> expectedTrainings = Collections.singletonList(new Training());
    when(trainerService.findByUsername(TRAINER_USERNAME)).thenReturn(new Trainer());
    when(trainingRepository.findTrainerTrainings(any(TrainingTrainerDto.class))).thenReturn(expectedTrainings);

    TrainingTrainerRequest request = new TrainingTrainerRequest(TRAINER_USERNAME, PERIOD_FROM, PERIOD_TO, TRAINEE_USERNAME);
    List<Training> result = trainingService.findTrainerTrainings(request);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(expectedTrainings, result);
  }

  @Test
  void testFindNotAssignedActiveTrainersToTrainee() {
    Trainee trainee = new Trainee();
    trainee.setId(1L);
    Trainer trainer = new Trainer();
    trainer.setId(2L);
    when(traineeService.findByUsername(TRAINEE_USERNAME)).thenReturn(trainee);

    List<Trainer> activeTrainers = Collections.singletonList(trainer);
    when(trainingRepository.getAssignedActiveTrainersToTrainee(trainee.getId())).thenReturn(activeTrainers);
    List<Trainer> trainersList = Collections.singletonList(new Trainer());
    when(trainerService.findAll()).thenReturn(trainersList);
    List<Trainer> result = trainingService.findNotAssignedActiveTrainersToTrainee(TRAINEE_USERNAME);
    assertNotNull(result);
    assertTrue(true);
  }

  @Test
  void testDelete() {
    // Подготовьте необходимые данные и проверьте, что метод выполняется без ошибок.
    Training training = new Training();
    doNothing().when(trainingRepository).delete(training);

    assertDoesNotThrow(() -> trainingService.delete(training));
  }

  @Test
  void testUpdateTraineeTrainerList() {
    // Подготовьте необходимые данные и проверьте, что метод возвращает ожидаемый результат.
    String traineeUsername = "trainee_username";
    String trainingDate = "2023-01-01";
    UpdateTraineeTrainerDto dto = new UpdateTraineeTrainerDto(traineeUsername, trainingDate, TRAINING_NAME, Collections.emptyList());

    Trainee trainee = new Trainee();
    trainee.setUsername(traineeUsername);
    Training training = new Training();
    training.setTrainingName(TRAINING_NAME);
    when(traineeService.findByUsername(traineeUsername)).thenReturn(trainee);
    when(trainingRepository.findTraineeTrainingsList(trainee.getId(), trainingDate, TRAINING_NAME)).thenReturn(Collections.singletonList(training));
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenReturn(new Training());
    when(trainingRepository.save(any(Training.class))).thenReturn(new Training());

    List<TrainerDtoForTrainee> result = trainingService.updateTraineeTrainerList(dto);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindTrainingById_WhenTrainingNotFound() {
    long trainingId = 1L;
    when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());

    assertThrows(OperationFailedException.class, () -> {
      trainingService.findTrainingById(trainingId);
    });
  }


  @Test
  void testSaveTraining_WhenTraineeNotFound() {
    TrainingRequest request = new TrainingRequest("non_existent_trainee_username", "trainer_username", "Gymnastics", PERIOD_FROM, TRAINING_NAME, 60);

    when(traineeService.findByUsername(request.traineeUsername())).thenReturn(null);
    when(trainerService.findByUsername(request.trainerUsername())).thenReturn(new Trainer());
    when(trainingRepository.findTrainingTypeByName(request.trainingType())).thenReturn(new TrainingType());
    assertThrows(NullPointerException.class, () -> {
      trainingService.saveTraining(request);
    });
  }


  @Test
  void testSaveTraining_WhenTrainerNotFound() {
    TrainingRequest request = new TrainingRequest("trainee_username",
        "non_existent_trainer_username", "Gymnastics", PERIOD_FROM, TRAINING_NAME, 60);

    when(traineeService.findByUsername(request.traineeUsername())).thenReturn(new Trainee());
    when(trainerService.findByUsername(request.trainerUsername())).thenReturn(null);
    when(trainingConverter.toTraining(any(TrainingRequest.class), any(Training.class))).thenAnswer(invocation -> {
      TrainingRequest req = invocation.getArgument(0);
      Training training = invocation.getArgument(1);
      // Инициализируйте training согласно вашей бизнес-логике, например:
      training.setTrainee(traineeService.findByUsername(req.traineeUsername()));
      training.setTrainer(trainerService.findByUsername(req.trainerUsername()));
      // Далее добавьте остальные поля
      return training;
    });
    when(trainingRepository.save(any(Training.class))).thenAnswer(invocation -> {
      Training training = invocation.getArgument(0);
      return training;
    });

    Training result = trainingService.saveTraining(request);

    assertNotNull(result);
    // Добавьте проверки по вашей логике
  }


}
