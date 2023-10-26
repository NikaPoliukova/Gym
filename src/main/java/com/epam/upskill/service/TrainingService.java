package com.epam.upskill.service;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingService {
  Optional<Training> findTrainingById(long trainingId);

  Training saveTraining(TrainingDto trainingDto);

  List<Training> findTrainingsByUsernameAndCriteria(String username, String criteria);

  List<Trainer> findNotAssignedActiveTrainersToTrainee(long traineeId);
}
