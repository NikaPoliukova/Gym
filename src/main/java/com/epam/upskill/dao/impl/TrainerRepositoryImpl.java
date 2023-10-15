package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.storage.TrainerStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class TrainerRepositoryImpl implements TrainerRepository {
  private TrainerStorage trainerStorage;

  @Autowired
  public TrainerRepositoryImpl(TrainerStorage trainerStorage) {
    this.trainerStorage = trainerStorage;
  }

  @Override
  public void create(Trainer trainer) {
    log.debug("Creating Trainer: " + trainer);
    trainerStorage.save(trainer);
  }

  @Override
  public Trainer findById(long id) {
    log.debug("Finding Trainer by ID: " + id);
    return trainerStorage.findById(id);
  }

  @Override
  public Map<Long, Trainer> findAll() {
    log.debug("Fetching all Trainers");
    return trainerStorage.getTrainerMap();
  }

  @Override
  public void updateTrainer(Trainer trainer) {
    log.debug("Updating Trainer: " + trainer);
    trainerStorage.updateTrainer(trainer);
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    log.debug("Deleting Trainer by ID: " + trainerId);
    trainerStorage.deleteTrainerById(trainerId);
  }
}

