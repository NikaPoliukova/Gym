package com.epam.upskill.dao.impl;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.storage.TraineeStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class TraineeRepositoryImplTest {

  @InjectMocks
  private TraineeRepositoryImpl traineeRepository;

  @Mock
  private TraineeStorage traineeStorage;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testCreateTrainee() {
    Trainee trainee = new Trainee();
    traineeRepository.create(trainee);
    verify(traineeStorage, times(1)).save(trainee);
  }

  @Test
  public void testFindTraineeById() {
    long traineeId = 1;
    when(traineeStorage.findById(traineeId)).thenReturn(new Trainee());
    Trainee foundTrainee = traineeRepository.findById(traineeId);
    assertNotNull(foundTrainee);
  }

  @Test
  public void testFindAllTrainees() {
    Map<Long, Trainee> traineeMap = Collections.singletonMap(1L, new Trainee());
    when(traineeStorage.getTraineeMap()).thenReturn(traineeMap);
    Map<Long, Trainee> allTrainees = traineeRepository.findAll();
    assertEquals(traineeMap, allTrainees);
  }

  @Test
  public void testUpdateTrainee() {
    Trainee trainee = new Trainee();
    traineeRepository.updateTrainee(trainee);
    verify(traineeStorage, times(1)).updateTrainee(trainee);
  }

  @Test
  public void testDeleteTraineeById() {
    long traineeId = 1;
    traineeRepository.deleteTraineeById(traineeId);
    verify(traineeStorage, times(1)).deleteTraineeById(traineeId);
  }
}


