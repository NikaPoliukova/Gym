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

public class TraineeServiceImplTest {
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
  public void testGetTraineeById() {
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
  public void testCreateTrainee() {
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
  public void testUpdateTrainee() {
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
  public void testDeleteTraineeById() {
    // Arrange
    long traineeId = 1;

    // Act
    traineeService.deleteTraineeById(traineeId);

    // Assert
    verify(traineeRepository, times(1)).deleteTraineeById(traineeId);
  }

  @Test
  public void testGetNonExistentTrainee() {
    // Arrange
    long nonExistentTraineeId = 999;
    when(traineeRepository.findById(nonExistentTraineeId)).thenReturn(null);

    // Act
    Trainee resultTrainee = traineeService.getTraineeById(nonExistentTraineeId);

    // Assert
    assertNull(resultTrainee);
  }

  @Test
  public void testCreateTraineeWithDuplicateUsername() {
    // Arrange
    TraineeRegistration traineeRegistration = new TraineeRegistration("Jimnov", "Liin", "Street22", new Date());
    Map<Long, Trainee> traineeMap = new HashMap<>();
    traineeMap.put(1L, new Trainee());
    when(traineeRepository.findAll()).thenReturn(traineeMap);
    when(trainerRepository.findAll()).thenReturn(Collections.emptyMap());

    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.createTrainee(traineeRegistration));
  }

  @Test
  public void testUpdateNonExistentTrainee() {
    // Arrange
    TraineeDto traineeDto = new TraineeDto(999, "newPassword", "newAddress");
    when(traineeRepository.findById(traineeDto.id())).thenReturn(null);

    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.updateTrainee(traineeDto));
  }

  @Test
  public void testDeleteTraineeWithFailure() {
    // Arrange
    long traineeId = 1;
    doThrow(new RuntimeException("Failed to delete Trainee")).when(traineeRepository).deleteTraineeById(traineeId);

    // Act and Assert
    assertThrows(Exception.class, () -> traineeService.deleteTraineeById(traineeId));
  }
}
