package com.epam.upskill.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.converter.TraineeConverter;
import upskill.dao.TrainerRepository;
import upskill.dao.TrainingRepository;
import upskill.dto.TraineeDtoForTrainer;
import upskill.dto.TrainerRegistration;
import upskill.dto.TrainerUpdateRequest;
import upskill.dto.TrainingTypeEnum;
import upskill.entity.Trainee;
import upskill.entity.Trainer;
import upskill.entity.TrainingType;
import upskill.exception.UserNotFoundException;
import upskill.service.HashPassService;
import upskill.service.UserService;
import upskill.service.impl.TrainerServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class TrainerServiceImplTest {

  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final String SPECIALIZATION = "YOGA";
  public static final String USERNAME = "john_doe";
  public static final Trainer TRAINER = new Trainer();
  public static final long TRAINER_ID = 1L;
  public static final String PASSWORD = "password";
  @InjectMocks
  private TrainerServiceImpl trainerService;

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private UserService userService;

  @Mock
  private TraineeConverter traineeConverter;

  @Mock
  private TrainingRepository trainingRepository;
  @Mock
  private HashPassService hashPassService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindById() {
    TRAINER.setId(TRAINER_ID);
    when(trainerRepository.findById(TRAINER_ID)).thenReturn(Optional.of(TRAINER));
    Trainer result = trainerService.findById(TRAINER_ID);
    assertEquals(TRAINER, result);
  }

  @Test
  void testFindById_UserNotFoundException() {
    when(trainerRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> {
      trainerService.findById(1L);
    });
  }

  @Test
  void testFindByUsername() {
    TRAINER.setUsername(USERNAME);
    when(trainerRepository.findByUsername(USERNAME)).thenReturn(Optional.of(TRAINER));
    Trainer result = trainerService.findByUsername(USERNAME);
    assertEquals(TRAINER, result);
  }

  @Test
  void testFindByUsernameAndPassword_ExistingTrainer() {
    String hashedPassword = "hashedPassword";
    Trainer mockTrainer = new Trainer();
    mockTrainer.setUsername(USERNAME);
    mockTrainer.setPassword(hashedPassword);
    // Мокируем поведение hashPassService
    when(hashPassService.hashPass(PASSWORD)).thenReturn(hashedPassword);
    // Мокируем поведение trainerRepository
    when(trainerRepository.findByUsernameAndPassword(USERNAME, hashedPassword)).thenReturn(Optional.of(mockTrainer));
    // Act
    Trainer resultTrainer = trainerService.findByUsernameAndPassword(USERNAME, PASSWORD);

    // Assert
    assertEquals(mockTrainer, resultTrainer);
  }

  @Test
  void testFindAll() {
    List<Trainer> trainers = new ArrayList<>();
    trainers.add(new Trainer());
    trainers.add(new Trainer());
    when(trainerRepository.findAll()).thenReturn(trainers);
    List<Trainer> result = trainerService.findAll();
    assertEquals(trainers, result);
  }

  @Test
  void testSaveTrainer() {
    TrainerRegistration registration = TrainerRegistration.builder().firstName(FIRST_NAME).lastName(LAST_NAME)
        .specialization(SPECIALIZATION).build();
    TrainingType trainingType = new TrainingType();
    trainingType.setTrainingTypeName(TrainingTypeEnum.YOGA);
    when(trainingRepository.findTrainingTypeByName(registration.getSpecialization())).thenReturn(trainingType);
    when(userService.findAll()).thenReturn(Collections.emptyList());
    when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());
    var result = trainerService.saveTrainer(registration);
    assertNotNull(result);
    assertEquals(registration.getFirstName(), FIRST_NAME);
    assertEquals(registration.getLastName(), LAST_NAME);

  }

  @Test
  void testUpdate() {
    TrainerUpdateRequest request = new TrainerUpdateRequest(USERNAME, FIRST_NAME, LAST_NAME, SPECIALIZATION, true);
    TRAINER.setUsername(USERNAME);
    when(trainerRepository.findByUsername(USERNAME)).thenReturn(Optional.of(TRAINER));
    when(trainerRepository.update(TRAINER)).thenReturn(Optional.of(TRAINER));
    Trainer result = trainerService.update(request);
    assertNotNull(result);
    verify(trainerRepository).update(TRAINER);
  }

  @Test
  void testFindByIsActive() {
    List<Trainer> activeTrainers = new ArrayList<>();
    activeTrainers.add(new Trainer());
    when(trainerRepository.findByIsActive()).thenReturn(activeTrainers);
    List<Trainer> result = trainerService.findByIsActive();
    assertEquals(activeTrainers, result);
  }

  @Test
  void testToggleProfileActivation() {
    boolean isActive = false;
    when(trainerRepository.findById(TRAINER_ID)).thenReturn(Optional.of(TRAINER));
    trainerService.toggleProfileActivation(TRAINER_ID, isActive);
    assertEquals(isActive, TRAINER.isActive());
    verify(trainerRepository).toggleProfileActivation(TRAINER);
  }

  @Test
  void testFindTraineesForTrainer() {
    List<Trainee> trainees = new ArrayList<>();
    trainees.add(new Trainee());
    when(trainerRepository.findTraineesForTrainer(TRAINER_ID)).thenReturn(trainees);
    when(traineeConverter.toTraineeDtoForTrainer(trainees)).thenReturn(Collections.emptyList());
    List<TraineeDtoForTrainer> result = trainerService.findTraineesForTrainer(TRAINER_ID);
    assertNotNull(result);
  }
}
