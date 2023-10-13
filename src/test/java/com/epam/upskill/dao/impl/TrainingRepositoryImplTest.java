package com.epam.upskill.dao.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TrainingStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainingRepositoryImplTest {

  @InjectMocks
  private TrainingRepositoryImpl trainingRepository;

  @Mock
  private TrainingStorage trainingStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTraining() {
    // Arrange
    Training training = new Training();

    // Act
    trainingRepository.create(training);

    // Assert
    verify(trainingStorage, times(1)).save(training);
  }

  @Test
  public void testFindTrainingById() {
    // Arrange
    long trainingId = 1;
    Training expectedTraining = new Training();
    when(trainingStorage.findById(trainingId)).thenReturn(expectedTraining);

    // Act
    Training foundTraining = trainingRepository.findById(trainingId);

    // Assert
    assertNotNull(foundTraining);
    assertEquals(expectedTraining, foundTraining);
  }

  @Test
  public void testFindAllTrainings() {
    // Arrange
    Map<Long, Training> trainingMap = Collections.singletonMap(1L, new Training());
    when(trainingStorage.getTrainingMap()).thenReturn(trainingMap);

    // Act
    Map<Long, Training> allTrainings = trainingRepository.findAll();

    // Assert
    assertEquals(trainingMap, allTrainings);
  }
}

