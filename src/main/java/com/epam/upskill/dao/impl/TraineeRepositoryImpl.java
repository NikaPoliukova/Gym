package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.storage.TraineeStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@Component
public class TraineeRepositoryImpl implements TraineeRepository {
  private TraineeStorage traineeStorage;

  @Autowired
  public void setTraineeStorage(TraineeStorage traineeStorage) {
    this.traineeStorage = traineeStorage;
  }

  @Override
  public void create(Trainee trainee) {
    traineeStorage.save(trainee);
  }

  @Override
  public Trainee findById(long id) {
    return traineeStorage.findById(id);
  }

  @Override
  public Map<Long, Trainee> findAll() {
    return traineeStorage.getTraineeMap();
  }

  @Override
  public void updateTrainee(Trainee trainee) {
    traineeStorage.updateTrainee(trainee);
  }

  @Override
  public void deleteTraineeById(long traineeId) {
    traineeStorage.deleteTraineeById(traineeId);
  }
}
