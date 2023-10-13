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
    Trainer trainer = new Trainer();
    trainerRepository.create(trainer);
    verify(trainerStorage, times(1)).save(trainer);
  }

  @Test
  public void testFindTrainerById() {
    long trainerId = 1;
    when(trainerStorage.findById(trainerId)).thenReturn(new Trainer());
    Trainer foundTrainer = trainerRepository.findById(trainerId);
    assertNotNull(foundTrainer);
  }

  @Test
  public void testFindAllTrainers() {
    Map<Long, Trainer> trainerMap = Collections.singletonMap(1L, new Trainer());
    when(trainerStorage.getTrainerMap()).thenReturn(trainerMap);
    Map<Long, Trainer> allTrainers = trainerRepository.findAll();
    assertEquals(trainerMap, allTrainers);
  }

  @Test
  public void testUpdateTrainer() {
    Trainer trainer = new Trainer();
    trainerRepository.updateTrainer(trainer);
    verify(trainerStorage, times(1)).updateTrainer(trainer);
  }

  @Test
  public void testDeleteTrainerById() {
    long trainerId = 1;
    trainerRepository.deleteTrainerById(trainerId);
    verify(trainerStorage, times(1)).deleteTrainerById(trainerId);
  }
}
