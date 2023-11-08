package com.epam.upskill.service;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;

public interface TrainingService {
  Training findTrainingById(long trainingId);

  Training saveTraining(TrainingRequest trainingRequest);

  List<Training> findTrainingsByUsernameAndCriteria(TrainingTraineeRequest request);

  List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName);

  List<Training> findTrainerTrainings(TrainingTrainerRequest request);

  List<Trainer> findNotAssignedActiveTrainersToTrainee(String username);

  List<TrainingType> findTrainingTypes();

  void delete(Training training);

  List<TrainerDtoForTrainee> updateTraineeTrainerList(UpdateTraineeTrainerDto dto);
}
