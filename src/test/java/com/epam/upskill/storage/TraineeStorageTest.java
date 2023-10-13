package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraineeStorageTest {

  @InjectMocks
  private TraineeStorage traineeStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    traineeStorage = new TraineeStorage();
  }

  @Test
  public void testSaveTrainee() {
    Trainee trainee = new Trainee();
    trainee.setId(1);

    traineeStorage.save(trainee);

    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(1, traineeMap.size());
    assertEquals(trainee, traineeMap.get(1L));
  }

  @Test
  public void testFindTraineeById() {
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);

    traineeStorage.save(trainee);

    Trainee foundTrainee = traineeStorage.findById(traineeId);
    assertEquals(trainee, foundTrainee);
  }

  @Test
  public void testFindTraineeByIdNotFound() {
    long traineeId = 1;

    Trainee foundTrainee = traineeStorage.findById(traineeId);
    assertEquals(null, foundTrainee);
  }

  @Test
  public void testUpdateTrainee() {
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);

    traineeStorage.save(trainee);

    Trainee updatedTrainee = new Trainee();
    updatedTrainee.setId(traineeId);
    updatedTrainee.setFirstName("UpdatedName");

    traineeStorage.updateTrainee(updatedTrainee);

    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals("UpdatedName", traineeMap.get(1L).getFirstName());
  }

  @Test
  public void testUpdateTraineeNotFound() {
    long traineeId = 1;
    Trainee updatedTrainee = new Trainee();
    updatedTrainee.setId(traineeId);
    updatedTrainee.setFirstName("UpdatedName");

    traineeStorage.updateTrainee(updatedTrainee);

    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }

  @Test
  public void testDeleteTraineeById() {
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);

    traineeStorage.save(trainee);

    traineeStorage.deleteTraineeById(traineeId);

    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }

  @Test
  public void testDeleteTraineeByIdNotFound() {
    long traineeId = 1;

    traineeStorage.deleteTraineeById(traineeId);

    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }
}
