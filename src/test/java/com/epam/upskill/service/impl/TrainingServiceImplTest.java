//package com.epam.upskill.service.impl;
//
//import com.epam.upskill.converter.TrainingConverter;
//import com.epam.upskill.dao.TrainingRepository;
//import com.epam.upskill.dto.TrainingDto;
//import com.epam.upskill.entity.*;
//import com.epam.upskill.service.TraineeService;
//import com.epam.upskill.service.TrainerService;
//import com.epam.upskill.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//class TrainingServiceImplTest {
//
//  @InjectMocks
//  private TrainingServiceImpl trainingService;
//
//  @Mock
//  private TrainingRepository trainingRepository;
//
//  @Mock
//  private UserService userService;
//
//  @Mock
//  private TraineeService traineeService;
//
//  @Mock
//  private TrainerService trainerService;
//
//  @Mock
//  private TrainingConverter trainingConverter;
//  @BeforeEach
//  public void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }
//  @Test
//  void testFindTrainingById() {
//    long trainingId = 1L;
//    Training training = new Training();
//    when(trainingRepository.findById(trainingId)).thenReturn(Optional.of(training));
//
//   Training result = trainingService.findTrainingById(trainingId);
//
//    assertTrue(result!=null);
//    assertEquals(training, result);
//  }
//
//  @Test
//  void testFindTrainingById_TrainingNotFound() {
//    long trainingId = 1L;
//    when(trainingRepository.findById(trainingId)).thenReturn(Optional.empty());
//
//   Training result = trainingService.findTrainingById(trainingId);
//
//    assertTrue(result == null);
//  }
//
//  @Test
//  void testSaveTraining() {
//    TrainingDto trainingDto = new TrainingDto("TrainingName", LocalDate.now(), 60, 1, 2, 3);
//    Trainee trainee = new Trainee();
//    Trainer trainer = new Trainer();
//    TrainingType trainingType = new TrainingType();
//    Training training = new Training();
//
//    when(traineeService.findById(trainingDto.traineeId())).thenReturn(Optional.of(trainee));
//    when(trainerService.findById(trainingDto.trainerId())).thenReturn(Optional.of(trainer));
//    when(trainingRepository.findTrainingTypeById(trainingDto.trainingTypeId())).thenReturn(trainingType);
//    when(trainingConverter.toTraining(eq(trainingDto), any(Training.class))).thenReturn(training);
//    when(trainingRepository.save(training)).thenReturn(training);
//
//    Training result = trainingService.saveTraining(trainingDto);
//
//    assertNotNull(result);
//    assertEquals(training, result);
//    assertEquals(trainee, result.getTrainee());
//    assertEquals(trainer, result.getTrainer());
//    assertEquals(trainingType, result.getTrainingType());
//  }
//
//  @Test
//  void testFindTrainingsByUsernameAndCriteria() {
//    String username = "john_doe";
//    String criteria = "search_criteria";
//    User user = new User();
//    when(userService.findByUsername(username)).thenReturn(user);
//
//    List<Training> trainings = new ArrayList<>();
//    trainings.add(new Training());
//    //when(trainingRepository.findTrainingsByUsernameAndCriteria(username, criteria)).thenReturn(trainings);
//
//    List<Training> result = trainingService.findTrainingsByUsernameAndCriteria(username, criteria);
//
//    assertFalse(result.isEmpty());
//    assertEquals(trainings, result);
//  }
//
//  @Test
//  void testFindTrainingsByUsernameAndCriteria_UserNotFound() {
//    String username = "john_doe";
//    when(userService.findByUsername(username)).thenReturn(null);
//
//    List<Training> result = trainingService.findTrainingsByUsernameAndCriteria(username, "search_criteria");
//
//    assertTrue(result.isEmpty());
//  }
//
//  @Test
//  void testFindNotAssignedActiveTrainersToTrainee() {
//    long traineeId = 1L;
//    Trainee trainee = new Trainee();
//    when(traineeService.findById(traineeId)).thenReturn(trainee);
//
//    List<Trainer> trainers = new ArrayList<>();
//    trainers.add(new Trainer());
//    when(trainingRepository.getNotAssignedActiveTrainersToTrainee(traineeId)).thenReturn(trainers);
//
//    List<Trainer> result = trainingService.findNotAssignedActiveTrainersToTrainee(traineeId);
//
//    assertFalse(result.isEmpty());
//    assertEquals(trainers, result);
//  }
//
//  @Test
//  void testFindNotAssignedActiveTrainersToTrainee_TraineeNotFound() {
//    long traineeId = 1L;
//    when(traineeService.findById(traineeId)).thenReturn(null);
//
//    List<Trainer> result = trainingService.findNotAssignedActiveTrainersToTrainee(traineeId);
//
//    assertTrue(result.isEmpty());
//  }
//}