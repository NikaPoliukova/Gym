package com.epam.upskill.service;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;

public interface TrainingService {
  Training findTrainingById(long trainingId);

  Training saveTraining(TrainingRequest trainingRequest);

  List<Training> findTrainingsByUsernameAndCriteria(String username, String password, String periodFrom, String periodTo,
                                                    String trainerName, String trainingType);

  List<Trainer> findNotAssignedActiveTrainersToTrainee(long traineeId);

  List<TrainingType> findTrainingTypes();
}
