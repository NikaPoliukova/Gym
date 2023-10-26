package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
  Optional<Trainer> findById(long trainerId);

  Trainer findByUsername(String username);

  List<Trainer> findAll();

  Trainer updateTrainerPassword(TrainerDto trainerDto);

  void saveTrainer(TrainerRegistration trainerDto);

  Trainer updateTrainer(TrainerDto trainerDto);

  void deleteTrainerById(long trainerId);

  List<Trainer> findByIsActive();

  void toggleProfileActivation(long userId);
}
