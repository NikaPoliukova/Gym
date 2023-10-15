package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;

public class TrainingServiceImplTest {
  @InjectMocks
  private TrainingServiceImpl trainingService;

  @Mock
  private TrainingRepository trainingRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetTrainingById() {
    // Arrange
    long trainingId = 1;
    Training expectedTraining = new Training();
    expectedTraining.setId(trainingId);
    Mockito.when(trainingRepository.findById(trainingId)).thenReturn(expectedTraining);

    // Act
    Training resultTraining = trainingService.getTrainingById(trainingId);

    // Assert
    Assertions.assertEquals(expectedTraining, resultTraining);
  }

  @Test
  public void testGetTrainingByIdNotFound() {
    // Arrange
    long trainingId = 1;
    Mockito.when(trainingRepository.findById(trainingId)).thenReturn(null);

    // Act
    Training resultTraining = trainingService.getTrainingById(trainingId);

    // Assert
    Assertions.assertNull(resultTraining);
  }

  @Test
  public void testCreateTraining() {
    // Arrange
    TrainingDto trainingDto = new TrainingDto("Training Name", new Date(), 120, 1);

    // Act and Assert
    Assertions.assertDoesNotThrow(() -> trainingService.createTraining(trainingDto));
  }
}
