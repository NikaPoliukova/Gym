package com.epam.upskill.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.controller.RegistrationController;
import upskill.dto.Principal;
import upskill.dto.TraineeRegistration;
import upskill.dto.TrainerRegistration;
import upskill.exception.RegistrationException;
import upskill.service.TraineeService;
import upskill.service.TrainerService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RegistrationControllerTest {

  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
  public static final String ADDRESS = "address";
  public static final String SPECIALIZATION = "Fitness";
  @InjectMocks
  private RegistrationController registrationController;

  @Mock
  private TraineeService traineeService;

  @Mock
  private TrainerService trainerService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void testTraineeRegistrationSuccess() {
    TraineeRegistration traineeRegistration = getTraineeRegistration();
    var principal = new Principal("username", "password12");
    when(traineeService.saveTrainee(traineeRegistration)).thenReturn(principal);
    registrationController.traineeRegistration(traineeRegistration);

  }

  private static TraineeRegistration getTraineeRegistration() {
    TraineeRegistration traineeRegistration = new TraineeRegistration();
    traineeRegistration.setFirstName(FIRST_NAME);
    traineeRegistration.setLastName(LAST_NAME);
    traineeRegistration.setAddress(ADDRESS);
    traineeRegistration.setDateOfBirth(DATE_OF_BIRTH);
    return traineeRegistration;
  }

  @Test
  void testTraineeRegistrationFailure() {
    TraineeRegistration traineeRegistration = getTraineeRegistration();
    when(traineeService.saveTrainee(traineeRegistration)).thenThrow(new RegistrationException());
    assertThrows(RegistrationException.class, () -> {
      registrationController.traineeRegistration(traineeRegistration);
    });
  }

  @Test
  void testTrainerRegistrationFailure() {
    TrainerRegistration trainerRegistration = getTrainerRegistration();
    when(trainerService.saveTrainer(trainerRegistration)).thenThrow(new RegistrationException());
    assertThrows(RegistrationException.class, () -> {
      registrationController.trainerRegistration(trainerRegistration);
    });
  }

  @Test
  void testTrainerRegistrationSuccess() {
    TrainerRegistration trainerRegistration = getTrainerRegistration();
    var trainer = new Principal("username", "password12");
    when(trainerService.saveTrainer(trainerRegistration)).thenReturn(trainer);
    registrationController.trainerRegistration(trainerRegistration);
  }

  private static TrainerRegistration getTrainerRegistration() {
    TrainerRegistration trainerRegistration = new TrainerRegistration();
    trainerRegistration.setFirstName(FIRST_NAME);
    trainerRegistration.setLastName(LAST_NAME);
    trainerRegistration.setSpecialization(SPECIALIZATION);
    return trainerRegistration;
  }


}
