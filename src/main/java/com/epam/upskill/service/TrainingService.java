package com.epam.upskill.service;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;

public interface TrainingService {
  Training getTrainingById(long traineeId);

  void createTraining(TrainingDto trainingDto);

}
