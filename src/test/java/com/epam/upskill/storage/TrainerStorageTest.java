package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainerStorageTest {

  @InjectMocks
  private TrainerStorage trainerStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    trainerStorage = new TrainerStorage();
  }

  @Test
  public void testSaveTrainer() {
    // Arrange
    Trainer trainer = new Trainer();
    trainer.setId(1);

    // Act
    trainerStorage.save(trainer);

    // Assert
    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(1, trainerMap.size());
    assertEquals(trainer, trainerMap.get(1L));
  }

  @Test
  public void testFindTrainerById() {
    // Arrange
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);

    // Act
    trainerStorage.save(trainer);

    // Assert
    Trainer foundTrainer = trainerStorage.findById(trainerId);
    assertEquals(trainer, foundTrainer);
  }
  @Test
  public void testFindTrainerByIdNotFound() {
    // Arrange
    long trainerId = 1;
    // Act and
    Trainer foundTrainer = trainerStorage.findById(trainerId);
    assertEquals(null, foundTrainer);
  }

  @Test
  public void testUpdateTrainer() {
    // Arrange
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainerStorage.save(trainer);

    Trainer updatedTrainer = new Trainer();
    updatedTrainer.setId(trainerId);
    updatedTrainer.setFirstName("UpdatedName");

    // Act
    trainerStorage.updateTrainer(updatedTrainer);

    // Assert
    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals("UpdatedName", trainerMap.get(1L).getFirstName());
  }

  @Test
  public void testUpdateTrainerNotFound() {
    // Arrange
    long trainerId = 1;
    Trainer updatedTrainer = new Trainer();
    updatedTrainer.setId(trainerId);
    updatedTrainer.setFirstName("UpdatedName");

    // Act
    trainerStorage.updateTrainer(updatedTrainer);

    // Assert
    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }

  @Test
  public void testDeleteTrainerById() {
    // Arrange
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);
    trainerStorage.save(trainer);

    // Act
    trainerStorage.deleteTrainerById(trainerId);

    // Assert
    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }

  @Test
  public void testDeleteTrainerByIdNotFound() {
    // Arrange
    long trainerId = 1;

    // Act
    trainerStorage.deleteTrainerById(trainerId);

    // Assert
    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }
}
