package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDtoForTrainer;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;

import java.util.List;

public interface TrainerService {
  Trainer findById(long trainerId);

  Trainer findByUsername(String username);

  Trainer findByUsernameAndPassword(String username, String password);

  List<Trainer> findAll();

  Trainer saveTrainer(TrainerRegistration trainerDto);

  Trainer updateTrainer(TrainerDto trainerDto);

  List<Trainer> findByIsActive();

  void toggleProfileActivation(long userId);

  List<TraineeDtoForTrainer> findTraineesForTrainer(long id);
}
