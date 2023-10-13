package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

public interface TrainerService {
  Trainer getTrainerById(long trainerId);

  void createTrainer(TrainerRegistration trainerDto);

  void updateTrainer(TrainerDto trainerDto);

  void deleteTrainerById(long trainerId);
}
