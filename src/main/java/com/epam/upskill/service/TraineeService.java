package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;

import java.util.List;

public interface TraineeService {
  Trainee getTraineeById(long traineeId);

  Trainee getTraineeByUsername(String username);

  void updateTraineePassword(TraineeDto traineeDto);

  void createTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  void updateTrainee(TraineeDto trainee);

  void deleteTraineeById(long traineeId);
}
