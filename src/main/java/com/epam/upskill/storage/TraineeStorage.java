package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainee;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TraineeStorage {
  private Map<Long, Trainee> traineeMap = new HashMap<>();

  public Map<Long, Trainee> getTraineeMap() {
    return traineeMap;
  }

  public void save(Trainee trainee) {
    traineeMap.put(trainee.getId(), trainee);
  }

  public Trainee findById(long id) {
    return traineeMap.get(id);
  }

  public void updateTrainee(Trainee updateTrainee) {
    traineeMap.put(updateTrainee.getId(), updateTrainee);
  }

  public void deleteTraineeById(long traineeId) {
    traineeMap.remove(traineeId);
  }
}
