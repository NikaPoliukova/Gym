package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.storage.TraineeStorage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class TraineeRepositoryImpl implements TraineeRepository {
  private static final Logger logger = LoggerFactory.getLogger(TraineeRepositoryImpl.class);
  private TraineeStorage traineeStorage;

  @Autowired
  public void setTraineeStorage(TraineeStorage traineeStorage) {
    this.traineeStorage = traineeStorage;
  }

  @Override
  public void create(Trainee trainee) {
    logger.debug("Creating Trainee: " + trainee);
    traineeStorage.save(trainee);
  }

  @Override
  public Trainee findById(long id) {
    logger.debug("Finding Trainee by ID: " + id);
    return traineeStorage.findById(id);
  }

  @Override
  public Map<Long, Trainee> findAll() {
    logger.debug("Fetching all Trainees");
    return traineeStorage.getTraineeMap();
  }

  @Override
  public void updateTrainee(Trainee trainee) {
    logger.debug("Updating Trainee: " + trainee);
    traineeStorage.updateTrainee(trainee);
  }

  @Override
  public void deleteTraineeById(long traineeId) {
    logger.debug("Deleting Trainee by ID: " + traineeId);
    traineeStorage.deleteTraineeById(traineeId);
  }
}

