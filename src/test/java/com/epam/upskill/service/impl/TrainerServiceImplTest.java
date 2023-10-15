package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
  public void testDeleteTrainerSuccess() {
    // Arrange
    long trainerId = 1;
    doNothing().when(trainerRepository).deleteTrainerById(trainerId);

    // Act
    trainerService.deleteTrainerById(trainerId);
  }

  @Test
  public void testDeleteTrainerWithFailure() {
    // Arrange
    long trainerId = 1;
    doThrow(new RuntimeException("Failed to delete Trainer")).when(trainerRepository).deleteTrainerById(trainerId);
    trainerService.deleteTrainerById(trainerId);
  }
}

