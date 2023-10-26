package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
  Optional<Trainer> findById(long trainerId);

  Optional<Trainer> findByUsername(String username);

  List<Trainer> findAll();

  Optional<Trainer> saveTrainer(TrainerRegistration trainerDto);

  Optional<Trainer> updateTrainer(TrainerDto trainerDto);

  List<Trainer> findByIsActive();

  void toggleProfileActivation(long userId);
}
