package com.epam.upskill.storage;

import com.epam.upskill.entity.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TrainingStorage {

  private Map<Long, Training> trainingMap = new HashMap<>();

  public Map<Long, Training> getTrainingMap() {
    return trainingMap;
  }

  public void save(Training training) {
    trainingMap.put(training.getId(), training);
    log.debug("Training saved: " + training);
  }

  public Training findById(long id) {
    Training training = trainingMap.get(id);
    if (training != null) {
      log.debug("Found Training by ID: " + id);
    } else {
      log.warn("Training not found for ID: " + id);
    }
    return training;
  }
}
