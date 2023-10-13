package com.epam.upskill.storage;

import static org.junit.jupiter.api.Assertions.*;

import com.epam.upskill.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
    Trainer trainer = new Trainer();
    trainer.setId(1);

    trainerStorage.save(trainer);

    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(1, trainerMap.size());
    assertEquals(trainer, trainerMap.get(1L));
  }

  @Test
  public void testFindTrainerById() {
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);

    trainerStorage.save(trainer);

    Trainer foundTrainer = trainerStorage.findById(trainerId);
    assertEquals(trainer, foundTrainer);
  }

  @Test
  public void testFindTrainerByIdNotFound() {
    long trainerId = 1;

    Trainer foundTrainer = trainerStorage.findById(trainerId);
    assertEquals(null, foundTrainer);
  }

  @Test
  public void testUpdateTrainer() {
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);

    trainerStorage.save(trainer);

    Trainer updatedTrainer = new Trainer();
    updatedTrainer.setId(trainerId);
    updatedTrainer.setFirstName("UpdatedName");

    trainerStorage.updateTrainer(updatedTrainer);

    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals("UpdatedName", trainerMap.get(1L).getFirstName());
  }

  @Test
  public void testUpdateTrainerNotFound() {
    long trainerId = 1;
    Trainer updatedTrainer = new Trainer();
    updatedTrainer.setId(trainerId);
    updatedTrainer.setFirstName("UpdatedName");

    trainerStorage.updateTrainer(updatedTrainer);

    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }

  @Test
  public void testDeleteTrainerById() {
    long trainerId = 1;
    Trainer trainer = new Trainer();
    trainer.setId(trainerId);

    trainerStorage.save(trainer);

    trainerStorage.deleteTrainerById(trainerId);

    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }

  @Test
  public void testDeleteTrainerByIdNotFound() {
    long trainerId = 1;

    trainerStorage.deleteTrainerById(trainerId);

    Map<Long, Trainer> trainerMap = trainerStorage.getTrainerMap();
    assertEquals(0, trainerMap.size());
  }
}
