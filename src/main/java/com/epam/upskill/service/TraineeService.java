package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;

import java.util.List;

public interface TraineeService {
  Trainee findById(long traineeId);

  Trainee findByUsername(String username);

  Trainee findByUsernameAndPassword(String username, String password);

  Trainee saveTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  Trainee updateTrainee(TraineeUpdateRequest traineeUpdateRequest);

  void toggleProfileActivation(long userId,boolean isActive);
  List<TrainerDtoForTrainee> findTrainersForTrainee(long id);
}
