package com.epam.upskill.facade;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingFacade {
  private final TrainingService trainingService;

  @Autowired
  public TrainingFacade(TrainingService trainingService) {
    this.trainingService = trainingService;
  }

  public Training getTrainingById(long trainingId) {
    return trainingService.getTrainingById(trainingId);
  }

  public void createTraining(TrainingDto trainingDto) {
    trainingService.createTraining(trainingDto);
  }
}
