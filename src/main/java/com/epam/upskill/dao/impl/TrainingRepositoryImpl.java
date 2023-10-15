package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TrainingStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {
  private TrainingStorage trainingStorage;

  @Autowired
  public TrainingRepositoryImpl(TrainingStorage trainingStorage) {
    this.trainingStorage = trainingStorage;
  }

  @Override
  public void create(Training training) {
    log.debug("Creating Training: " + training);
    trainingStorage.save(training);
  }

  @Override
  public Training findById(long id) {
    log.debug("Finding Training by ID: " + id);
    return trainingStorage.findById(id);
  }

  @Override
  public Map<Long, Training> findAll() {
    log.debug("Fetching all Trainings");
    return trainingStorage.getTrainingMap();
  }
}

