package com.epam.upskill.config;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TraineeStorage;
import com.epam.upskill.storage.TrainerStorage;
import com.epam.upskill.storage.TrainingStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class DataInitializer {

  @Value("${data.traineePathFile}")
  private String traineePathFile;

  @Value("${data.trainerPathFile}")
  private String trainerPathFile;

  @Value("${data.trainingPathFile}")
  private String trainingPathFile;

  private TraineeStorage traineeStorage;

  private TrainerStorage trainerStorage;

  private TrainingStorage trainingStorage;

  private Mapper mapper;

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

  @Autowired
  public void setMapper(Mapper mapper) {
    this.mapper = mapper;
  }

  @PostConstruct
  public void initializeData() {


    log.info("PostConstruct method was started");
    List<Trainee> traineeData = mapper.mapJsonToListTrainee(traineePathFile);
    List<Trainer> trainerData = mapper.mapJsonToListTrainer(trainerPathFile);
    List<Training> trainingData = mapper.mapJsonToListTraining(trainingPathFile);
    for (Trainee trainee : traineeData) {
      traineeStorage.save(trainee);
    }
    for (Trainer trainer : trainerData) {
      trainerStorage.save(trainer);
    }
    for (Training training : trainingData) {
      trainingStorage.save(training);
    }

    log.info("Map1: " + traineeStorage.getTraineeMap());
    log.info("Map2: " + trainerStorage.getTrainerMap());
    log.info("Map3: " + trainingStorage.getTrainingMap());
    log.info("PostConstruct method was finished successfully");
  }
}
