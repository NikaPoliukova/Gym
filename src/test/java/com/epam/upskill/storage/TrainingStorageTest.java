package com.epam.upskill.storage;

import com.epam.upskill.entity.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingStorageTest {

  @InjectMocks
  private TrainingStorage trainingStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    trainingStorage = new TrainingStorage();
  }

  @Test
  public void testSaveTraining() {
    Training training = new Training();
    training.setId(1);

    trainingStorage.save(training);

    Map<Long, Training> trainingMap = trainingStorage.getTrainingMap();
    assertEquals(1, trainingMap.size());
    assertEquals(training, trainingMap.get(1L));
  }

  @Test
  public void testFindTrainingById() {
    long trainingId = 1;
    Training training = new Training();
    training.setId(trainingId);

    trainingStorage.save(training);

    Training foundTraining = trainingStorage.findById(trainingId);
    assertEquals(training, foundTraining);
  }

  @Test
  public void testFindTrainingByIdNotFound() {
    long trainingId = 1;

    Training foundTraining = trainingStorage.findById(trainingId);
    assertEquals(null, foundTraining);
  }
}