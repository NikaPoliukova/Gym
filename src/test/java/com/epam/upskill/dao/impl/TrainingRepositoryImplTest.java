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
    Training training = new Training();
    trainingRepository.create(training);
    verify(trainingStorage, times(1)).save(training);
  }

  @Test
  public void testFindTrainingById() {
    long trainingId = 1;
    when(trainingStorage.findById(trainingId)).thenReturn(new Training());
    Training foundTraining = trainingRepository.findById(trainingId);
    assertNotNull(foundTraining);
  }

  @Test
  public void testFindAllTrainings() {
    Map<Long, Training> trainingMap = Collections.singletonMap(1L, new Training());
    when(trainingStorage.getTrainingMap()).thenReturn(trainingMap);
    Map<Long, Training> allTrainings = trainingRepository.findAll();
    assertEquals(trainingMap, allTrainings);
  }
}
