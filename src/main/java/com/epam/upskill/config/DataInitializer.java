package com.epam.upskill.config;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TraineeStorage;
import com.epam.upskill.storage.TrainerStorage;
import com.epam.upskill.storage.TrainingStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;


@Component
public class DataInitializer {
  private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

  private TraineeStorage traineeStorage;

  private TrainerStorage trainerStorage;

  private TrainingStorage trainingStorage;

  private final Mapper mapper;

  @Autowired
  public void setTraineeStorage(TraineeStorage traineeStorage) {
    this.traineeStorage = traineeStorage;
  }

  @Autowired
  public void setTrainerStorage(TrainerStorage trainerStorage) {
    this.trainerStorage = trainerStorage;
  }

  @Autowired
  public void setTrainingStorage(TrainingStorage trainingStorage) {
    this.trainingStorage = trainingStorage;
  }

  public DataInitializer(Mapper mapper) {
    this.mapper = mapper;
  }

  @PostConstruct
  public void initializeData() {
    Logger logger = LoggerFactory.getLogger(getClass());

    logger.info("PostConstruct method is executed.");
    Resource traineePathFile = new ClassPathResource("data/trainee.json");
    Resource trainerPathFile = new ClassPathResource("data/trainer.json");
    Resource trainingPathFile = new ClassPathResource("data/training.json");
    try {
      List<Trainee> traineeData = mapper.mapJsonToListTrainee(traineePathFile.getFile().getAbsolutePath());
      List<Trainer> trainerData = mapper.mapJsonToListTrainer(trainerPathFile.getFile().getAbsolutePath());
      List<Training> trainingData = mapper.mapJsonToListTraining(trainingPathFile.getFile().getAbsolutePath());
      for (Trainee trainee : traineeData) {
        traineeStorage.save(trainee);
      }
      for (Trainer trainer : trainerData) {
        trainerStorage.save(trainer);
      }
      for (Training training : trainingData) {
        trainingStorage.save(training);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("Map1: " + traineeStorage.getTraineeMap());
    logger.info("Map2: " + trainerStorage.getTrainerMap());
    logger.info("Map3: " + trainingStorage.getTrainingMap());
    logger.info("PostConstruct method was finished  with success");
  }
}
