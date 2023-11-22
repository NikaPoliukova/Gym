package com.epam.upskill.service;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.security.Principal;

import java.util.List;

public interface TraineeService {
  Trainee findById(long traineeId);

  Trainee findByUsername(String username);

  Trainee findByUsernameAndPassword(String username, String password);

  Principal saveTrainee(TraineeRegistration trainee);

  List<Trainee> findAll();

  Trainee updateTrainee(TraineeUpdateRequest traineeUpdateRequest);

  void toggleProfileActivation(long userId, boolean isActive);

  List<TrainerDtoForTrainee> findTrainersForTrainee(long id);
}
