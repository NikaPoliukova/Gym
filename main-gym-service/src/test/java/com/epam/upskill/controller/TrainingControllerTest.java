//package com.epam.upskill.controller;
//
//import com.epam.upskill.converter.TrainingTypeConverter;
//import com.epam.upskill.dto.TrainingRequest;
//import com.epam.upskill.dto.TrainingTypeResponse;
//import com.epam.upskill.entity.Training;
//import com.epam.upskill.entity.TrainingType;
//import com.epam.upskill.exception.OperationFailedException;
//import com.epam.upskill.service.TrainingService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//class TrainingControllerTest {
//
//  public static final String TRAINEE_USER = "traineeUser";
//  public static final String TRAINER_USER = "trainerUser";
//  public static final String TRAINING_TYPE = "YOGA";
//  public static final LocalDate DATE = LocalDate.of(2023, 11, 9);
//  public static final String TRAINING_NAME = "Training 1";
//  public static final int DURATION = 60;
//  @InjectMocks
//  private TrainingController trainingController;
//
//  @Mock
//  private TrainingService trainingService;
//
//  @Mock
//  private TrainingTypeConverter converter;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @Test
//  void testSaveTrainingSuccess() {
//    TrainingRequest trainingRequest = new TrainingRequest(TRAINEE_USER, TRAINER_USER, TRAINING_NAME, DATE, TRAINING_TYPE,
//        DURATION);
//    when(trainingService.saveTraining(trainingRequest)).thenReturn(new Training());
//    trainingController.saveTraining(TRAINEE_USER, TRAINER_USER, TRAINING_NAME, DATE, TRAINING_TYPE, DURATION);
//  }
//
//  @Test
//  void testSaveTrainingFailure() {
//    TrainingRequest trainingRequest = new TrainingRequest(TRAINEE_USER, TRAINER_USER, TRAINING_NAME, DATE, TRAINING_TYPE,
//        DURATION);
//    when(trainingService.saveTraining(trainingRequest)).thenThrow(new OperationFailedException(TRAINING_NAME +
//        "with date " + DATE, "Save training"));
//    assertThrows(OperationFailedException.class, () -> {
//      trainingController.saveTraining(TRAINEE_USER, TRAINER_USER, TRAINING_NAME, DATE, TRAINING_TYPE, DURATION);
//    });
//  }
//
//  @Test
//  void testGetTrainingTypes() {
//    TrainingType trainingType = new TrainingType();
//    TrainingType trainingType2 = new TrainingType();
//    List<TrainingType> trainingTypes = Arrays.asList(trainingType, trainingType2);
//    when(trainingService.findTrainingTypes()).thenReturn(trainingTypes);
//    when(converter.toTrainingTypeResponse(trainingTypes)).thenReturn(Arrays.asList
//        (new TrainingTypeResponse(2, TRAINING_TYPE),
//            new TrainingTypeResponse(3, "PILATES")));
//    List<TrainingTypeResponse> result = trainingController.getTrainingTypes();
//  }
//}
