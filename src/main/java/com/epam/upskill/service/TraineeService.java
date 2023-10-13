package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;

public interface TraineeService {
  Trainee getTraineeById(long traineeId);

  void createTrainee(TraineeRegistration trainee);

  void updateTrainee(TraineeDto trainee);

  void deleteTraineeById(long traineeId);
}
