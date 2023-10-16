package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.Map;

public interface TrainerService {
  Trainer getTrainerById(long trainerId);

  Map<Long, Trainer> findAll();

  void createTrainer(TrainerRegistration trainerDto);

  void updateTrainer(TrainerDto trainerDto);

  void deleteTrainerById(long trainerId);
}
