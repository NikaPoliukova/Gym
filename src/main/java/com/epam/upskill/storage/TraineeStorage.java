package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TraineeStorage {

  private Map<Long, Trainee> traineeMap = new HashMap<>();

  public Map<Long, Trainee> getTraineeMap() {
    return traineeMap;
  }


  public void save(Trainee trainee) {
    traineeMap.put(trainee.getId(), trainee);
    log.debug("Trainee saved: " + trainee);
  }

  public Trainee findById(long id) {
    Trainee trainee = traineeMap.get(id);
    if (trainee != null) {
      log.debug("Found Trainee by ID: " + id);
    } else {
      log.warn("Trainee not found for ID: " + id);
    }
    return trainee;
  }

  public void updateTrainee(Trainee updatedTrainee) {
    long id = updatedTrainee.getId();
    if (traineeMap.containsKey(id)) {
      traineeMap.put(id, updatedTrainee);
      log.debug("Trainee updated: " + updatedTrainee);
    } else {
      log.warn("Trainee not found for update: ID " + id);
    }
  }

  public void deleteTraineeById(long traineeId) {
    if (traineeMap.containsKey(traineeId)) {
      traineeMap.remove(traineeId);
      log.debug("Trainee deleted: ID " + traineeId);
    } else {
      log.warn("Trainee not found for delete: ID " + traineeId);
    }
  }
}

