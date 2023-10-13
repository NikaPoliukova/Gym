package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TrainingStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainingRepositoryImpl implements TrainingRepository {
  private static final Logger logger = LoggerFactory.getLogger(TrainingRepositoryImpl.class);
  private TrainingStorage trainingStorage;

  @Autowired
  public void setTrainingStorage(TrainingStorage trainingStorage) {
    this.trainingStorage = trainingStorage;
  }

  @Override
  public void create(Training training) {
    logger.debug("Creating Training: " + training);
    trainingStorage.save(training);
  }

  @Override
  public Training findById(long id) {
    logger.debug("Finding Training by ID: " + id);
    return trainingStorage.findById(id);
  }

  @Override
  public Map<Long, Training> findAll() {
    logger.debug("Fetching all Trainings");
    return trainingStorage.getTrainingMap();
  }
}

