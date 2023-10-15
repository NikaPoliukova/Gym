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
    MockitoAnnotations.openMocks(this);
    traineeStorage = new TraineeStorage();
  }

  @Test
  public void testSaveTrainee() {
    // Arrange
    Trainee trainee = new Trainee();
    trainee.setId(1);

    // Act
    traineeStorage.save(trainee);

    // Assert
    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(1, traineeMap.size());
    assertEquals(trainee, traineeMap.get(1L));
  }

  @Test
  public void testFindTraineeById() {
    // Arrange
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);

    // Act
    traineeStorage.save(trainee);

    // Assert
    Trainee foundTrainee = traineeStorage.findById(traineeId);
    assertEquals(trainee, foundTrainee);
  }

  @Test
  public void testFindTraineeByIdNotFound() {
    // Arrange
    long traineeId = 1;
    // Act  and Assert
    Trainee foundTrainee = traineeStorage.findById(traineeId);
    assertEquals(null, foundTrainee);
  }

  @Test
  public void testUpdateTrainee() {
    // Arrange
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    traineeStorage.save(trainee);

    Trainee updatedTrainee = new Trainee();
    updatedTrainee.setId(traineeId);
    updatedTrainee.setFirstName("UpdatedName");

    // Act
    traineeStorage.updateTrainee(updatedTrainee);

    // Assert
    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals("UpdatedName", traineeMap.get(1L).getFirstName());
  }

  @Test
  public void testUpdateTraineeNotFound() {
    // Arrange
    long traineeId = 1;
    Trainee updatedTrainee = new Trainee();
    updatedTrainee.setId(traineeId);
    updatedTrainee.setFirstName("UpdatedName");

    // Act
    traineeStorage.updateTrainee(updatedTrainee);

    // Assert
    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }

  @Test
  public void testDeleteTraineeById() {
    // Arrange
    long traineeId = 1;
    Trainee trainee = new Trainee();
    trainee.setId(traineeId);
    traineeStorage.save(trainee);

    // Act
    traineeStorage.deleteTraineeById(traineeId);

    // Assert
    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }

  @Test
  public void testDeleteTraineeByIdNotFound() {
    // Arrange
    long traineeId = 1;

    // Act
    traineeStorage.deleteTraineeById(traineeId);

    // Assert
    Map<Long, Trainee> traineeMap = traineeStorage.getTraineeMap();
    assertEquals(0, traineeMap.size());
  }
}
