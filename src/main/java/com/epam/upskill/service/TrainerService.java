package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
  Trainer findById(long trainerId);

  Trainer findByUsername(String username);

  List<Trainer> findAll();

  Trainer saveTrainer(TrainerRegistration trainerDto);

  Trainer updateTrainer(TrainerDto trainerDto);

  List<Trainer> findByIsActive();

  void toggleProfileActivation(long userId);
}
