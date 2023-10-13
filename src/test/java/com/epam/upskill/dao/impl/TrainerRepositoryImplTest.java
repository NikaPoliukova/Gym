package com.epam.upskill.dao.impl;

import com.epam.upskill.entity.Trainer;
import com.epam.upskill.storage.TrainerStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TrainerRepositoryImplTest {

  @InjectMocks
  private TrainerRepositoryImpl trainerRepository;

  @Mock
  private TrainerStorage trainerStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTrainer() {
    // Arrange
    Trainer trainer = new Trainer();

    // Act
    trainerRepository.create(trainer);

    // Assert
    verify(trainerStorage, times(1)).save(trainer);
  }

  @Test
  public void testFindTrainerById() {
    // Arrange
    long trainerId = 1;
    Trainer expectedTrainer = new Trainer();
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
    Map<Long, Trainer> trainerMap = Collections.singletonMap(1L, new Trainer());
    when(trainerStorage.getTrainerMap()).thenReturn(trainerMap);

    // Act
    Map<Long, Trainer> allTrainers = trainerRepository.findAll();

    // Assert
    assertEquals(trainerMap, allTrainers);
  }

  @Test
  public void testUpdateTrainer() {
    // Arrange
    Trainer trainer = new Trainer();

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
  }
}

