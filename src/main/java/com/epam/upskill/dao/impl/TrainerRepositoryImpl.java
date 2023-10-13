package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.storage.TrainerStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrainerRepositoryImpl implements TrainerRepository {
  private static final Logger logger = LoggerFactory.getLogger(TrainerRepositoryImpl.class);
  private TrainerStorage trainerStorage;

  @Autowired
  public void setTrainerStorage(TrainerStorage trainerStorage) {
    this.trainerStorage = trainerStorage;
  }

  @Override
  public void create(Trainer trainer) {
    logger.debug("Creating Trainer: " + trainer);
    trainerStorage.save(trainer);
  }

  @Override
  public Trainer findById(long id) {
    logger.debug("Finding Trainer by ID: " + id);
    return trainerStorage.findById(id);
  }

  @Override
  public Map<Long, Trainer> findAll() {
    logger.debug("Fetching all Trainers");
    return trainerStorage.getTrainerMap();
  }

  @Override
  public void updateTrainer(Trainer trainer) {
    logger.debug("Updating Trainer: " + trainer);
    trainerStorage.updateTrainer(trainer);
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    logger.debug("Deleting Trainer by ID: " + trainerId);
    trainerStorage.deleteTrainerById(trainerId);
  }
}

