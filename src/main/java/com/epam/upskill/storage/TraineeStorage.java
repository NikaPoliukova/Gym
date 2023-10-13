package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainee;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TraineeStorage {
  private static final Logger logger = LoggerFactory.getLogger(TraineeStorage.class);

  private Map<Long, Trainee> traineeMap = new HashMap<>();

  public Map<Long, Trainee> getTraineeMap() {
    return traineeMap;
  }

  public void save(Trainee trainee) {
    traineeMap.put(trainee.getId(), trainee);
    logger.debug("Trainee saved: " + trainee);
  }

  public Trainee findById(long id) {
    Trainee trainee = traineeMap.get(id);
    if (trainee != null) {
      logger.debug("Found Trainee by ID: " + id);
    } else {
      logger.warn("Trainee not found for ID: " + id);
    }
    return trainee;
  }

  public void updateTrainee(Trainee updatedTrainee) {
    long id = updatedTrainee.getId();
    if (traineeMap.containsKey(id)) {
      traineeMap.put(id, updatedTrainee);
      logger.debug("Trainee updated: " + updatedTrainee);
    } else {
      logger.warn("Trainee not found for update: ID " + id);
    }
  }

  public void deleteTraineeById(long traineeId) {
    if (traineeMap.containsKey(traineeId)) {
      traineeMap.remove(traineeId);
      logger.debug("Trainee deleted: ID " + traineeId);
    } else {
      logger.warn("Trainee not found for delete: ID " + traineeId);
    }
  }
}

