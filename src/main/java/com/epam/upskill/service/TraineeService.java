package com.epam.upskill.service;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;

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

//  List<TrainerDtoForTrainee> updateTraineeTrainerList(String username, String trainingDate, String trainingName, List<TrainersDtoList> list);


}
