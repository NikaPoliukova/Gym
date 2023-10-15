package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.storage.TraineeStorage;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@NoArgsConstructor
@Repository
public class TraineeRepositoryImpl implements TraineeRepository {
  private TraineeStorage traineeStorage;

  @Autowired
  public TraineeRepositoryImpl(TraineeStorage traineeStorage) {
    this.traineeStorage = traineeStorage;
  }

  @Override
  public void create(Trainee trainee) {
    log.debug("Creating Trainee: " + trainee);
    traineeStorage.save(trainee);
  }

  @Override
  public Trainee findById(long id) {
    log.debug("Finding Trainee by ID: " + id);
    return traineeStorage.findById(id);
  }

  @Override
  public Map<Long, Trainee> findAll() {
    log.debug("Fetching all Trainees");
    return traineeStorage.getTraineeMap();
  }

  @Override
  public void updateTrainee(Trainee trainee) {
    log.debug("Updating Trainee: " + trainee);
    traineeStorage.updateTrainee(trainee);
  }

  @Override
  public void deleteTraineeById(long traineeId) {
    log.debug("Deleting Trainee by ID: " + traineeId);
    traineeStorage.deleteTraineeById(traineeId);
  }
}

