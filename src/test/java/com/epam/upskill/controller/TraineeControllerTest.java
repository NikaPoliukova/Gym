package com.epam.upskill.controller;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainingService;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TraineeControllerTest {
  public static final String USERNAME = "testUser";
  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final String ADDRESS = "Address";
  public static final String TRAINING_DATE = "2023-11-09";
  public static final String TRAINING_NAME = "Training";
  public static final String TRAINER_NAME = "Trainer";
  public static final LocalDate PERIOD_FROM = LocalDate.of(2023, 11, 1);
  public static final LocalDate PERIOD_TO = LocalDate.of(2023, 11, 10);
  public static final String TRAINING_TYPE = "Type";
  @InjectMocks
  private TraineeController traineeController;

  @Mock
  private UserService userService;

  @Mock
  private TraineeService traineeService;

  @Mock
  private TraineeConverter traineeConverter;

  @Mock
  private TrainerConverter trainerConverter;

  @Mock
  private TrainingConverter trainingConverter;

  @Mock
  private TrainingService trainingService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTrainee() {
    Trainee trainee = new Trainee();
    when(traineeService.findByUsername(USERNAME)).thenReturn(trainee);
    TraineeResponse expectedResponse = Mockito.mock(TraineeResponse.class);
    when(traineeConverter.toTraineeResponse(trainee, traineeService)).thenReturn(expectedResponse);
    TraineeResponse result = traineeController.getTrainee(USERNAME);
    assertEquals(expectedResponse, result);
  }

  @Test
  void testGetTraineeUserNotFoundException() {
    when(traineeService.findByUsername(USERNAME)).thenThrow(new UserNotFoundException(USERNAME));
    assertThrows(UserNotFoundException.class, () -> {
      traineeController.getTrainee(USERNAME);
    });
  }

  @Test
  void testUpdateTrainee() {
    TraineeUpdateRequest updateRequest = new TraineeUpdateRequest(USERNAME, FIRST_NAME, LAST_NAME, LocalDate.now(), "Address", true);
    Trainee trainee = new Trainee();
    when(traineeService.updateTrainee(updateRequest)).thenReturn(trainee);
    TraineeUpdateResponse expectedResponse = Mockito.mock(TraineeUpdateResponse.class);
    when(traineeConverter.toTraineeUpdateResponse(trainee, traineeService)).thenReturn(expectedResponse);
    TraineeUpdateResponse result = traineeController.updateTrainee(USERNAME, FIRST_NAME, LAST_NAME,
        LocalDate.now(), ADDRESS, true);
    assertEquals(expectedResponse, result);
  }

  @Test
  void testUpdateTraineeOperationFailedException() {
    TraineeUpdateRequest updateRequest = new TraineeUpdateRequest(USERNAME, FIRST_NAME, LAST_NAME,
        LocalDate.now(), ADDRESS, true);
    when(traineeService.updateTrainee(updateRequest)).thenThrow(new OperationFailedException(
        USERNAME, "update trainee"));
    assertThrows(OperationFailedException.class, () -> {
      traineeController.updateTrainee(USERNAME, FIRST_NAME, LAST_NAME, LocalDate.now(), ADDRESS, true);
    });
  }

  @Test
  void testDeleteTrainee() {
    Trainee trainee = new Trainee();
    when(traineeService.findByUsername(USERNAME)).thenReturn(trainee);
    traineeController.deleteTrainee(USERNAME);
    verify(userService).delete(trainee.getId());
  }

  @Test
  void testDeleteTraineeOperationFailedException() {
    when(traineeService.findByUsername(USERNAME)).thenThrow(new OperationFailedException(USERNAME, "delete trainee"));
    assertThrows(OperationFailedException.class, () -> {
      traineeController.deleteTrainee(USERNAME);
    });
  }

  @Test
  void testFindNotActiveTrainers() {
    List<Trainer> trainerList = new ArrayList<>();
    when(trainingService.findNotAssignedActiveTrainersToTrainee(USERNAME)).thenReturn(trainerList);
    List<TrainerDtoForTrainee> expectedList = new ArrayList<>();
    when(trainerConverter.toTrainerDtoForTrainee(trainerList)).thenReturn(expectedList);
    List<TrainerDtoForTrainee> result = traineeController.findNotActiveTrainers(USERNAME);
    assertEquals(expectedList, result);
  }

  @Test
  void testUpdateTrainerList() {
    List<TrainersDtoList> list = new ArrayList<>();
    List<TrainerDtoForTrainee> expectedList = new ArrayList<>();
    when(trainingService.updateTraineeTrainerList(new UpdateTraineeTrainerDto(USERNAME, TRAINING_DATE,
        TRAINING_NAME, list))).thenReturn(expectedList);
    List<TrainerDtoForTrainee> result = traineeController.updateTrainerList(USERNAME, TRAINING_DATE,
        TRAINING_NAME, list);
    assertEquals(expectedList, result);
  }

  @Test
  void testUpdateTrainerListOperationFailedException() {
    List<TrainersDtoList> list = new ArrayList<>();
    when(trainingService.updateTraineeTrainerList(new UpdateTraineeTrainerDto(USERNAME, TRAINING_DATE, TRAINING_NAME,
        list)))
        .thenThrow(new OperationFailedException(USERNAME, "update trainee's trainers"));
    assertThrows(OperationFailedException.class, () -> {
      traineeController.updateTrainerList(USERNAME, TRAINING_DATE, TRAINING_NAME, list);
    });
  }

  @Test
  void testFindTrainingsList() {
    TrainingTraineeRequest request = new TrainingTraineeRequest(USERNAME, PERIOD_FROM, PERIOD_TO, TRAINER_NAME,
        TRAINING_TYPE);
    List<Training> trainingList = new ArrayList<>();
    when(trainingService.findTrainingsByUsernameAndCriteria(request)).thenReturn(trainingList);
    List<TrainingTraineeResponse> expectedList = new ArrayList<>();
    when(trainingConverter.toTrainingResponse(trainingList)).thenReturn(expectedList);
    List<TrainingTraineeResponse> result = traineeController.findTrainingsList(USERNAME, PERIOD_FROM, PERIOD_TO,
        TRAINER_NAME, TRAINING_TYPE);
    assertEquals(expectedList, result);
  }

  @Test
  void testToggleActivationOperationFailedException() {
    when(traineeService.findByUsername(USERNAME)).thenThrow(new OperationFailedException(USERNAME,
        "activate or deactivate a trainee"));
    assertThrows(OperationFailedException.class, () -> {
      traineeController.toggleActivation(USERNAME, true);
    });
  }
}




