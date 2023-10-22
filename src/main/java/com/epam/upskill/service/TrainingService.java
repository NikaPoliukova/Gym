package com.epam.upskill.service;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;

import java.util.List;

public interface TrainingService {
  Training getTrainingById(long traineeId);

  void createTraining(TrainingDto trainingDto);

  List<Training> getTrainingsByUsernameAndCriteria(String username, String criteria);

  List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId);

}
