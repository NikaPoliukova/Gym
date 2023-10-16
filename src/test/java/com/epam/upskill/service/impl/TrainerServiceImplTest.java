package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrainerServiceImplTest {

  @InjectMocks
  private TrainerServiceImpl trainerService;

  @Mock
  private TrainerRepository trainerRepository;

  @Mock
  private TraineeRepository traineeRepository;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetTrainerByIdFound() {
    // Arrange
    long trainerId = 1;
    Trainer expectedTrainer = new Trainer();
    expectedTrainer.setId(trainerId);
    when(trainerRepository.findById(trainerId)).thenReturn(expectedTrainer);
    // Act
    Trainer resultTrainer = trainerService.getTrainerById(trainerId);
    // Assert
    assertEquals(expectedTrainer, resultTrainer);
  }

  @Test
  public void testGetTrainerByIdNotFound() {
    // Arrange
    long trainerId = 1;
    when(trainerRepository.findById(trainerId)).thenReturn(null);
    // Act
    Trainer resultTrainer = trainerService.getTrainerById(trainerId);
    // Assert
    assertNull(resultTrainer);
  }

  @Test
  public void testFindAll() {
    // Arrange
    Map<Long, Trainer> trainerMap = new HashMap<>();
    trainerMap.put(1L, new Trainer(1L, "John", "Doe", "john.doe",
        "password", true, 1L, "fitness"));
    trainerMap.put(2L, new Trainer(2L, "Alice", "Smith", "alice.smith",
        "password", true, 2L, "yoga"));
    when(trainerRepository.findAll()).thenReturn(trainerMap);
    // Act
    Map<Long, Trainer> result = trainerService.findAll();
    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.containsKey(1L));
    assertTrue(result.containsKey(2L));
    verify(trainerRepository, times(1)).findAll();
  }

  @Test
  public void testFindAllWithEmptyResult() {
    // Arrange
    when(trainerRepository.findAll()).thenReturn(Collections.emptyMap());
    // Act
    Map<Long, Trainer> result = trainerService.findAll();
    // Assert
    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(trainerRepository, times(1)).findAll();  // Проверяем вызов метода findAll()
  }

  @Test
  public void testCreateTrainer() {
    // Arrange
    TrainerRegistration trainerRegistration = new TrainerRegistration("Doe", "John",
        "Specialization");
    // Act and Assert
    assertDoesNotThrow(() -> trainerService.createTrainer(trainerRegistration));
  }

  @Test
  public void testUpdateTrainerFound() {
    // Arrange
    TrainerDto trainerDto = new TrainerDto(1, "newPassword", "newSpecialization");
    Trainer existingTrainer = new Trainer();
    existingTrainer.setId(1);
    when(trainerRepository.findById(trainerDto.id())).thenReturn(existingTrainer);
    // Act
    trainerService.updateTrainer(trainerDto);
  }

  @Test
  public void testDeleteTrainerWithFailure() {
    // Arrange
    long trainerId = 1;
    doThrow(new RuntimeException("Failed to delete Trainer")).when(trainerRepository).deleteTrainerById(trainerId);
    trainerService.deleteTrainerById(trainerId);
  }

  @Test
  public void testDeleteTrainerSuccess() {
    // Arrange
    long trainerId = 1;
    ArgumentCaptor<Long> trainerIdCaptor = ArgumentCaptor.forClass(Long.class);
    doNothing().when(trainerRepository).deleteTrainerById(trainerIdCaptor.capture());

    // Act
    trainerService.deleteTrainerById(trainerId);

    // Assert
    verify(trainerRepository).deleteTrainerById(trainerIdCaptor.getValue());
    assertEquals(trainerId, trainerIdCaptor.getValue());
  }
}

