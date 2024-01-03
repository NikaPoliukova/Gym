package com.epam.upskill.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.controller.TrainingController;
import upskill.converter.TrainingTypeConverter;
import upskill.dto.TrainingRequest;
import upskill.dto.TrainingTypeResponse;
import upskill.entity.Training;
import upskill.entity.TrainingType;
import upskill.exception.OperationFailedException;
import upskill.service.TrainingService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TrainingControllerTest {

  public static final String TRAINEE_USER = "traineeUser";
  public static final String TRAINER_USER = "trainerUser";
  public static final String TRAINING_TYPE = "YOGA";
  public static final LocalDate DATE = LocalDate.of(2023, 11, 9);
  public static final String TRAINING_NAME = "Training 1";
  public static final int DURATION = 60;
  @InjectMocks
  private TrainingController trainingController;

  @Mock
  private TrainingService trainingService;

  @Mock
  private TrainingTypeConverter converter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSaveTrainingSuccess() {
    TrainingRequest trainingRequest = getTrainingRequest();
    when(trainingService.saveTraining(trainingRequest)).thenReturn(new Training());
    trainingController.saveTraining(trainingRequest, "header");
  }


  @Test
  void testSaveTrainingFailure() {
    TrainingRequest trainingRequest = getTrainingRequest();
    when(trainingService.saveTraining(trainingRequest)).thenThrow(new OperationFailedException(TRAINING_NAME +
        "with date " + DATE, "Save training"));
    assertThrows(OperationFailedException.class, () -> {
      trainingController.saveTraining(trainingRequest, "header");
    });
  }

  @Test
  void testGetTrainingTypes() {
    TrainingType trainingType = new TrainingType();
    TrainingType trainingType2 = new TrainingType();
    List<TrainingType> trainingTypes = Arrays.asList(trainingType, trainingType2);
    when(trainingService.findTrainingTypes()).thenReturn(trainingTypes);
    when(converter.toTrainingTypeResponse(trainingTypes)).thenReturn(Arrays.asList
        (new TrainingTypeResponse(2, TRAINING_TYPE),
            new TrainingTypeResponse(3, "PILATES")));
    List<TrainingTypeResponse> result = trainingController.getTrainingTypes();
  }

  private static TrainingRequest getTrainingRequest() {
    return new TrainingRequest(TRAINEE_USER, TRAINER_USER, TRAINING_NAME, DATE, TRAINING_TYPE,
        DURATION);
  }
}
