package com.epam.upskill.storage;

import com.epam.upskill.entity.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainingStorage {
  private static final Logger logger = LoggerFactory.getLogger(TrainingStorage.class);

  private Map<Long, Training> trainingMap = new HashMap<>();

  public Map<Long, Training> getTrainingMap() {
    return trainingMap;
  }

  public void save(Training training) {
    trainingMap.put(training.getId(), training);
    logger.debug("Training saved: " + training);
  }

  public Training findById(long id) {
    Training training = trainingMap.get(id);
    if (training != null) {
      logger.debug("Found Training by ID: " + id);
    } else {
      logger.warn("Training not found for ID: " + id);
    }
    return training;
  }
}
