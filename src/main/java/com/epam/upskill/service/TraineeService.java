package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;

import java.util.List;

public interface TraineeService {
  Trainee findById(long traineeId);

  Trainee getTraineeByUsername(String username);

  Trainee updateTraineePassword(TraineeDto traineeDto);

  void createTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  Trainee updateTrainee(TraineeDto trainee);

  void deleteTraineeById(long traineeId);
  void toggleProfileActivation(long userId);
}
