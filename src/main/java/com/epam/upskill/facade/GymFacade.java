package com.epam.upskill.facade;

import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GymFacade {
  private static final Logger logger = LoggerFactory.getLogger(GymFacade.class);

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;

  @Autowired
  public GymFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
    this.traineeService = traineeService;
    this.trainerService = trainerService;
    this.trainingService = trainingService;


    if (logger.isDebugEnabled()) {
      logger.debug("GymFacade instance created.");
      logger.debug("TraineeService: " + traineeService);
      logger.debug("TrainerService: " + trainerService);
      logger.debug("TrainingService: " + trainingService);
    }
  }
}
