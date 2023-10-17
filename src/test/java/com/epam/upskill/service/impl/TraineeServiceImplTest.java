package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraineeServiceImplTest {
  @InjectMocks
  private TraineeServiceImpl traineeService;

  @Mock
  private TraineeRepository traineeRepository;

  @Mock
  private TrainerRepository trainerRepository;

  @BeforeEach
  public void setUp() {
    traineeRepository = mock(TraineeRepository.class);
    trainerRepository = mock(TrainerRepository.class);
    traineeService = new TraineeServiceImpl(traineeRepository, trainerRepository);
  }

  @Test
   void testGetTraineeById() {
    // Arrange
    long traineeId = 1;
    Trainee expectedTrainee = new Trainee();
    expectedTrainee.setId(traineeId);
    when(traineeRepository.findById(traineeId)).thenReturn(expectedTrainee);
    // Act
    Trainee resultTrainee = traineeService.getTraineeById(traineeId);
    // Assert
    assertEquals(expectedTrainee, resultTrainee);
  }

  @Test
  void testFindAllTrainees() {
    // Arrange
    Map<Long, Trainee> traineeMap = new HashMap<>();
    traineeMap.put(1L, new Trainee(1L, "John", "Doe", "John.Doe", "password",
        true, 1L, new Date(), "123 Main Street"));
    traineeMap.put(2L, new Trainee(3L, "John", "Doe", "John.Doe2", "password",
        true, 1L, new Date(), "123 Main Street"));
    var trainee1 = Trainee.builder()
        .id(1L)
        .address("123 Main Street")
        .date(new Date())
        .build();
    traineeMap.put(3L, trainee1);

    when(traineeRepository.findAll()).thenReturn(traineeMap);
    // Act
    Map<Long, Trainee> result = traineeService.findAll();
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  void testCreateTrainee() {
    // Arrange
    TraineeRegistration traineeRegistration = new TraineeRegistration(
        "Jimnov", "Liin", "Street22", new Date());
    Map<Long, Trainee> emptyTraineeList = new HashMap<>();
    when(traineeRepository.findAll()).thenAnswer(invocation -> emptyTraineeList);
    when(trainerRepository.findAll()).thenAnswer(invocation -> emptyTraineeList);
    // Act
    traineeService.createTrainee(traineeRegistration);
    // Assert
    verify(traineeRepository, times(1)).create(any(Trainee.class));
  }


  @Test
  void testUpdateTrainee() {
    // Arrange
    TraineeDto traineeDto = new TraineeDto(1, "newPassword", "newAddress");
    Trainee existingTrainee = new Trainee();
    existingTrainee.setId(1);
    when(traineeRepository.findById(traineeDto.id())).thenReturn(existingTrainee);

    // Act
    traineeService.updateTrainee(traineeDto);

    // Assert
    verify(traineeRepository, times(1)).updateTrainee(existingTrainee);
  }

  @Test
  void testDeleteTraineeById() {
    // Arrange
    long traineeId = 1;
    // Act
    traineeService.deleteTraineeById(traineeId);
    // Assert
    verify(traineeRepository, times(1)).deleteTraineeById(traineeId);
  }

  @Test
  void testGetNonExistentTrainee() {
    // Arrange
    long nonExistentTraineeId = 999;
    when(traineeRepository.findById(nonExistentTraineeId)).thenReturn(null);
    // Act
    Trainee resultTrainee = traineeService.getTraineeById(nonExistentTraineeId);
    // Assert
    assertNull(resultTrainee);
  }

  @Test
  void testCreateTraineeWithDuplicateUsername() {
    // Arrange
    TraineeRegistration traineeRegistration = new TraineeRegistration("Jimnov", "Liin",
        "Street22", new Date());
    Map<Long, Trainee> traineeMap = new HashMap<>();
    traineeMap.put(1L, new Trainee());
    when(traineeRepository.findAll()).thenReturn(traineeMap);
    when(trainerRepository.findAll()).thenReturn(Collections.emptyMap());
    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.createTrainee(traineeRegistration));
  }

  @Test
  void testUpdateNonExistentTrainee() {
    // Arrange
    TraineeDto traineeDto = new TraineeDto(999, "newPassword", "newAddress");
    when(traineeRepository.findById(traineeDto.id())).thenReturn(null);
    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.updateTrainee(traineeDto));
  }

  @Test
  void testDeleteTraineeWithFailure() {
    // Arrange
    long traineeId = 1;
    doThrow(new RuntimeException("Failed to delete Trainee")).when(traineeRepository).deleteTraineeById(traineeId);
    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.deleteTraineeById(traineeId));
  }
}
