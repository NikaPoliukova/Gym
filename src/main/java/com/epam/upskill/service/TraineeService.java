package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeService {
  Optional<Trainee> findById(long traineeId);

  Trainee findByUsername(String username);

  Trainee updateTraineePassword(TraineeDto traineeDto);

  void saveTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  Trainee updateTrainee(TraineeDto trainee);

  void deleteTraineeById(long traineeId);

  void toggleProfileActivation(long userId);
}
