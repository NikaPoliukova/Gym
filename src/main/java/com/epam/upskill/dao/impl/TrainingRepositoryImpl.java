package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Training;
import com.epam.upskill.storage.TrainingStorage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@AllArgsConstructor
@Component
public class TrainingRepositoryImpl implements TrainingRepository {
  private TrainingStorage trainingStorage;

  @Autowired
  public void setTrainingStorage(TrainingStorage trainingStorage) {
    this.trainingStorage = trainingStorage;
  }

  @Override
  public void create(Training training) {
    trainingStorage.save(training);
  }

  @Override
  public Training findById(long id) {
    return trainingStorage.findById(id);
  }

  @Override
  public Map<Long, Training> findAll() {
    return trainingStorage.getTrainingMap();
  }


}
