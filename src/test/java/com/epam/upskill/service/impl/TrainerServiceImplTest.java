package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TraineeDtoForTrainer;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.dto.TrainerUpdateRequest;
import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
  void testFindByUsernameAndPassword() {
    TRAINER.setUsername(USERNAME);
    when(trainerRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.of(TRAINER));
    Trainer result = trainerService.findByUsernameAndPassword(USERNAME, PASSWORD);
    assertEquals(TRAINER, result);
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
    TrainerRegistration registration = new TrainerRegistration(FIRST_NAME, LAST_NAME, SPECIALIZATION);
    TrainingType trainingType = new TrainingType();
    trainingType.setTrainingTypeName(TrainingTypeEnum.YOGA);
    when(trainingRepository.findTrainingTypeByName(registration.specialization())).thenReturn(trainingType);
    when(userService.findAll()).thenReturn(Collections.emptyList());
    when(trainerRepository.save(any(Trainer.class))).thenReturn(new Trainer());
    Trainer result = trainerService.saveTrainer(registration);
    result.setSpecialization(trainingType);
    result.setUsername(USERNAME);
    result.setPassword(PASSWORD);
    result.setActive(true);
    assertNotNull(result);
    assertEquals(registration.firstName(), FIRST_NAME);
    assertEquals(registration.lastName(), LAST_NAME);
    assertEquals(USERNAME, result.getUsername());
    assertEquals(PASSWORD, result.getPassword());
    assertTrue(result.isActive());
    assertEquals(trainingType, result.getSpecialization());
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
