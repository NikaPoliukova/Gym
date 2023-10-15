package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;

import java.util.Map;

public interface TraineeService {
  Trainee getTraineeById(long traineeId);

  void createTrainee(TraineeRegistration trainee);

  Map<Long, Trainee> findAll();

  void updateTrainee(TraineeDto trainee);

  void deleteTraineeById(long traineeId);
}
