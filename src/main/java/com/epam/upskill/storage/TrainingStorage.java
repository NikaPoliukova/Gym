package com.epam.upskill.storage;

import com.epam.upskill.entity.Training;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage {
  private Map<Long, Training> trainingMap = new HashMap<>();

  public Map<Long, Training> getTrainingMap() {
    return trainingMap;
  }

  public void save(Training training) {
    trainingMap.put(training.getId(), training);
  }

  public Training findById(long id) {
    return trainingMap.get(id);
  }
}