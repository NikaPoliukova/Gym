package com.epam.upskill.dao.impl;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.storage.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TraineeRepositoryImplTest {

  @InjectMocks
  private TraineeRepositoryImpl traineeRepository;

  @Mock
  private TraineeStorage traineeStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTrainee() {
    // Arrange
    Trainee trainee = new Trainee();

    // Act
    traineeRepository.create(trainee);

    // Assert
    verify(traineeStorage, times(1)).save(trainee);
  }

  @Test
  public void testFindTraineeById() {
    // Arrange
    long traineeId = 1;
    Trainee expectedTrainee = new Trainee();
    when(traineeStorage.findById(traineeId)).thenReturn(expectedTrainee);

    // Act
    Trainee foundTrainee = traineeRepository.findById(traineeId);

    // Assert
    assertNotNull(foundTrainee);
    assertEquals(expectedTrainee, foundTrainee);
  }

  @Test
  public void testFindAllTrainees() {
    // Arrange
    Map<Long, Trainee> traineeMap = Collections.singletonMap(1L, new Trainee());
    when(traineeStorage.getTraineeMap()).thenReturn(traineeMap);

    // Act
    Map<Long, Trainee> allTrainees = traineeRepository.findAll();

    // Assert
    assertEquals(traineeMap, allTrainees);
  }

  @Test
  public void testUpdateTrainee() {
    // Arrange
    Trainee trainee = new Trainee();

    // Act
    traineeRepository.updateTrainee(trainee);

    // Assert
    verify(traineeStorage, times(1)).updateTrainee(trainee);
  }

  @Test
  public void testDeleteTraineeById() {
    // Arrange
    long traineeId = 1;

    // Act
    traineeRepository.deleteTraineeById(traineeId);

    // Assert
    verify(traineeStorage, times(1)).deleteTraineeById(traineeId);
  }
}



