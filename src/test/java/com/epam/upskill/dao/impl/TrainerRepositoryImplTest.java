package com.epam.upskill.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TrainerRepositoryImplTest {

  @InjectMocks
  private TrainerRepositoryImpl trainerRepository;



  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }
/*
  @Test
  public void testCreateTrainer() {
    // Arrange
    var trainer = Trainer.builder().build();

    // Act
    trainerRepository.create(trainer);

    // Assert
    verify(trainerStorage, times(1)).save(trainer);
  }

  @Test
  public void testFindTrainerById() {
    // Arrange
    long trainerId = 1;
    Trainer expectedTrainer = Trainer.builder().build();
    when(trainerStorage.findById(trainerId)).thenReturn(expectedTrainer);

    // Act
    Trainer foundTrainer = trainerRepository.findById(trainerId);

    // Assert
    assertNotNull(foundTrainer);
    assertEquals(expectedTrainer, foundTrainer);
  }

  @Test
  public void testFindAllTrainers() {
    // Arrange
    Map<Long, Trainer> trainerMap = Collections.singletonMap(1L, Trainer.builder().build());
    when(trainerStorage.getTrainerMap()).thenReturn(trainerMap);

    // Act
    Map<Long, Trainer> allTrainers = trainerRepository.findAll();

    // Assert
    assertEquals(trainerMap, allTrainers);
  }

  @Test
  public void testUpdateTrainer() {
    // Arrange
    Trainer trainer = Trainer.builder().build();

    // Act
    trainerRepository.updateTrainer(trainer);

    // Assert
    verify(trainerStorage, times(1)).updateTrainer(trainer);
  }

  @Test
  public void testDeleteTrainerById() {
    // Arrange
    long trainerId = 1;

    // Act
    trainerRepository.deleteTrainerById(trainerId);

    // Assert
    verify(trainerStorage, times(1)).deleteTrainerById(trainerId);
  }*/
}

