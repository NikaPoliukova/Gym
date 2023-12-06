//package com.epam.upskill.controller;
//
//import com.epam.upskill.dto.TraineeRegistration;
//import com.epam.upskill.dto.TrainerRegistration;
//import com.epam.upskill.entity.Trainee;
//import com.epam.upskill.entity.Trainer;
//import com.epam.upskill.exception.RegistrationException;
//import com.epam.upskill.dto.Principal;
//import com.epam.upskill.service.TraineeService;
//import com.epam.upskill.service.TrainerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//class RegistrationControllerTest {
//
//  public static final String FIRST_NAME = "John";
//  public static final String LAST_NAME = "Doe";
//  public static final LocalDate DATE_OF_BIRTH = LocalDate.of(1990, 1, 1);
//  public static final String ADDRESS = "address";
//  public static final String SPECIALIZATION = "Fitness";
//  @InjectMocks
//  private RegistrationController registrationController;
//
//  @Mock
//  private TraineeService traineeService;
//
//  @Mock
//  private TrainerService trainerService;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//
//  @Test
//  void testTraineeRegistrationSuccess() {
//    TraineeRegistration traineeRegistration = new TraineeRegistration(FIRST_NAME, LAST_NAME, ADDRESS, DATE_OF_BIRTH);
//    var principal = new Principal("username", "password12");
//    when(traineeService.saveTrainee(traineeRegistration)).thenReturn(principal);
//    registrationController.traineeRegistration(FIRST_NAME, LAST_NAME, ADDRESS, DATE_OF_BIRTH);
//
//  }
//  @Test
//  void testTraineeRegistrationFailure() {
//    TraineeRegistration traineeRegistration = new TraineeRegistration(FIRST_NAME, LAST_NAME, ADDRESS, DATE_OF_BIRTH);
//    when(traineeService.saveTrainee(traineeRegistration)).thenThrow(new RegistrationException());
//    assertThrows(RegistrationException.class, () -> {
//      registrationController.traineeRegistration(FIRST_NAME, LAST_NAME, ADDRESS, DATE_OF_BIRTH);
//    });
//  }
//
//  @Test
//  void testTrainerRegistrationSuccess() {
//    TrainerRegistration trainerRegistration = new TrainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
//    var trainer = new Principal("username", "password12");
//    when(trainerService.saveTrainer(trainerRegistration)).thenReturn(trainer);
//    registrationController.trainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
//  }
//
//  @Test
//  void testTrainerRegistrationFailure() {
//    TrainerRegistration trainerRegistration = new TrainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
//    when(trainerService.saveTrainer(trainerRegistration)).thenThrow(new RegistrationException());
//    assertThrows(RegistrationException.class, () -> {
//      registrationController.trainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
//    });
//  }
//
//}
