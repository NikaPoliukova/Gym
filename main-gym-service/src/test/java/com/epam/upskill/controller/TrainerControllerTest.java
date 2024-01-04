package com.epam.upskill.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.controller.TrainerController;
import upskill.converter.TrainerConverter;
import upskill.converter.TrainingConverter;
import upskill.dto.*;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.exception.OperationFailedException;
import upskill.exception.UserNotFoundException;
import upskill.service.TrainerService;
import upskill.service.TrainingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TrainerControllerTest {
  public static final String USERNAME = "testUser";
  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final String SPECIALIZATION = "Fitness";
  public static final LocalDate PERIOD_FROM = LocalDate.of(2023, 11, 1);
  public static final LocalDate PERIOD_TO = LocalDate.of(2023, 11, 10);

  @InjectMocks
  private TrainerController trainerController;

  @Mock
  private TrainerService trainerService;

  @Mock
  private TrainerConverter trainerConverter;

  @Mock
  private TrainingService trainingService;

  @Mock
  private TrainingConverter trainingConverter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetTrainer() {
    Trainer trainer = new Trainer();
    trainer.setActive(true);
    trainer.setUsername(USERNAME);
    when(trainerService.findByUsername(USERNAME)).thenReturn(trainer);
    TrainerResponse trainerResponse = new TrainerResponse("Jon", "Doe",
        "YOGA", true, new ArrayList<>());
    when(trainerConverter.toTrainerResponse(trainer, trainerService)).thenReturn(trainerResponse);
    TrainerResponse result = trainerController.getTrainer(USERNAME);
    assertNotNull(result);

  }


  @Test
  void testUpdateTrainer() {
    TrainerUpdateRequest updateRequest =
        new TrainerUpdateRequest(USERNAME, FIRST_NAME, LAST_NAME, SPECIALIZATION, true);
    Trainer trainer = new Trainer();
    trainer.setActive(true);
    TrainerUpdateResponse expectedResponse = new TrainerUpdateResponse(USERNAME, FIRST_NAME, LAST_NAME,
        "YOGA", true, new ArrayList<>());
    trainer.setUsername(USERNAME);
    when(trainerService.update(updateRequest)).thenReturn(trainer);
    when(trainerConverter.toTrainerUpdateResponse(trainer, trainerService)).thenReturn(expectedResponse);
    TrainerUpdateResponse result =
        trainerController.updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, SPECIALIZATION, true);
    assertNotNull(result);
  }


  @Test
  void testUpdateTrainerOperationFailedException() {
    when(trainerService.update(any(TrainerUpdateRequest.class)))
        .thenThrow(new OperationFailedException(USERNAME, "update Trainer's profile"));
    assertThrows(OperationFailedException.class, () -> {
      trainerController.updateTrainer(USERNAME, FIRST_NAME, LAST_NAME, SPECIALIZATION, true);
    });
  }

  @Test
  void testGetTrainerUserNotFoundException() {
    when(trainerService.findByUsername(USERNAME)).thenThrow(new UserNotFoundException(USERNAME));
    assertThrows(UserNotFoundException.class, () -> {
      trainerController.getTrainer(USERNAME);
    });
  }

  @Test
  void testFindTrainerTrainingsList() {
    List<Training> trainingsList = new ArrayList<>();
    when(trainingService.findTrainerTrainings(any(TrainingTrainerRequest.class))).thenReturn(trainingsList);
    when(trainingConverter.toTrainerTrainingResponse(trainingsList)).thenReturn(new ArrayList<>());
    List<TrainingTrainerResponse> result = trainerController.findTrainerTrainingsList(USERNAME, PERIOD_FROM,
        PERIOD_TO, "Training");
    assertNotNull(result);
  }

  @Test
  void testToggleActivation() {
    Trainer trainer = new Trainer();
    trainer.setActive(true);
    trainer.setUsername(USERNAME);
    when(trainerService.findByUsername(USERNAME)).thenReturn(trainer);
    trainerController.toggleActivationTrainer(USERNAME, true);
  }
}