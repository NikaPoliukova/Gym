package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.storage.TrainerStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class TrainerRepositoryImpl implements TrainerRepository {
  private TrainerStorage trainerStorage;

  @Autowired
  public void setTrainerStorage(TrainerStorage trainerStorage) {
    this.trainerStorage = trainerStorage;
  }

  @Override
  public void create(Trainer trainer) {
    trainerStorage.save(trainer);
  }

  @Override
  public Trainer findById(long id) {
    return trainerStorage.findById(id);
  }

  @Override
  public Map<Long, Trainer> findAll() {
    return trainerStorage.getTrainerMap();
  }

  @Override
  public void updateTrainer(Trainer trainer) {
    trainerStorage.updateTrainer(trainer);
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    trainerStorage.deleteTrainerById(trainerId);
  }
}
