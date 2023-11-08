package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {
  public static final String USERNAME = "johndoe";
  public static final String PASSWORD = "password";
  public static final long TRAINEE_ID = 1L;

  @InjectMocks
  private TraineeServiceImpl traineeService;

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private TraineeConverter traineeConverter;

  @Mock
  private UserService userService;

  @Mock
  private TrainerConverter trainerConverter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }


  @Test
  void testFindById_ExistingTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      Trainee trainee = new Trainee(); // Создайте тестового Trainee
      when(traineeRepository.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));
      Trainee result = traineeService.findById(TRAINEE_ID);
      assertEquals(trainee, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
    }
  }


  @Test
  void testFindById_NonExistingTrainee() {
    when(traineeRepository.findById(TRAINEE_ID)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> traineeService.findById(TRAINEE_ID));
  }

  @Test
  void testFindByUsername_ExistingTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      Trainee trainee = new Trainee(); // Создайте тестового Trainee
      when(traineeRepository.findByUsername(USERNAME)).thenReturn(Optional.of(trainee));
      Trainee result = traineeService.findByUsername(USERNAME);
      assertEquals(trainee, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
    }
  }


  @Test
  void testFindByUsername_NonExistingTrainee() {
    when(traineeRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> traineeService.findByUsername(USERNAME));
  }

  @Test
  void testFindByUsernameAndPassword_ExistingTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      Trainee trainee = new Trainee(); // Создайте тестового Trainee
      when(traineeRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.of(trainee));
      Trainee result = traineeService.findByUsernameAndPassword(USERNAME, PASSWORD);
      assertEquals(trainee, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId); // Восстановите исходное значение
    }
  }

  @Test
  void testFindByUsernameAndPassword_NonExistingTrainee() {
    when(traineeRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> traineeService.findByUsernameAndPassword(USERNAME, PASSWORD));
  }

  @Test
  void testFindAll() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      List<Trainee> traineeList = Collections.singletonList(new Trainee());
      when(traineeRepository.findAll()).thenReturn(traineeList);
      List<Trainee> result = traineeService.findAll();
      assertEquals(traineeList, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
    }
  }

  @Test
  void testSaveTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      TraineeRegistration traineeDto = Mockito.mock(TraineeRegistration.class);
      when(traineeDto.firstName()).thenReturn("John");
      when(traineeDto.lastName()).thenReturn("Doe");
      Trainee trainee = new Trainee();
      when(userService.findAll()).thenReturn(Collections.emptyList());
      when(traineeConverter.toTrainee(traineeDto)).thenReturn(trainee);
      when(traineeRepository.save(trainee)).thenReturn(trainee);
      Trainee result = traineeService.saveTrainee(traineeDto);
      assertEquals(trainee, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
    }
  }


  @Test
  void testUpdateTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    try {
      TraineeUpdateRequest request = new TraineeUpdateRequest(USERNAME,"Nika", "Poli", LocalDate.now(),
          "address", true);
      Trainee trainee = new Trainee();
      trainee.setUsername(USERNAME);
      when(traineeRepository.findByUsername(any())).thenReturn(Optional.of(trainee));
      when(traineeRepository.update(any())).thenReturn(Optional.of(trainee));


      Trainee result = traineeService.updateTrainee(request);
      assertEquals(trainee, result);
    } finally {
      MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
    }
  }


  @Test
  void testToggleProfileActivation() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
   boolean isActive = true;
    Trainee trainee = new Trainee();
    trainee.setId(TRAINEE_ID);
    when(traineeRepository.findById(TRAINEE_ID)).thenReturn(Optional.of(trainee));
    doNothing().when(traineeRepository).toggleProfileActivation(trainee);
    traineeService.toggleProfileActivation(TRAINEE_ID, isActive);
    assertTrue(trainee.isActive());
    verify(traineeRepository).findById(TRAINEE_ID);
    verify(traineeRepository).toggleProfileActivation(trainee);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
  }


  @Test
  void testFindTrainersForTrainee() {
    String originalTransactionId = MDC.get(TraineeServiceImpl.TRANSACTION_ID);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, "transactionId");
    List<Trainer> trainerList = Collections.singletonList(new Trainer());
    when(traineeRepository.findTrainersForTrainee(TRAINEE_ID)).thenReturn(trainerList);
    when(trainerConverter.toTrainerDtoForTrainee(trainerList)).thenReturn(Collections.emptyList());
    List<TrainerDtoForTrainee> result = traineeService.findTrainersForTrainee(TRAINEE_ID);
    assertEquals(Collections.emptyList(), result);
    MDC.put(TraineeServiceImpl.TRANSACTION_ID, originalTransactionId);
  }
}