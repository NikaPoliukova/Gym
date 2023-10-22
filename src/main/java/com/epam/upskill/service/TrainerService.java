package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.List;

public interface TrainerService {
  Trainer getTrainerById(long trainerId);

  Trainer getTrainerByUsername(String username);

  List<Trainer> findAll();

  void updateTrainerPassword(TrainerDto trainerDto);

  void createTrainer(TrainerRegistration trainerDto);

  void updateTrainer(TrainerDto trainerDto);

  void deleteTrainerById(long trainerId);

  List<Trainer> findByIsActive(boolean isActive);
}
