package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainerStorage {
  private Map<Long, Trainer> trainerMap = new HashMap<>();

  public Map<Long, Trainer> getTrainerMap() {
    return trainerMap;
  }

  public void save(Trainer trainer) {
    trainerMap.put(trainer.getId(), trainer);
  }

  public Trainer findById(long id) {
    return trainerMap.get(id);
  }

  public void updateTrainer(Trainer updateTrainer) {
    trainerMap.put(updateTrainer.getId(), updateTrainer);
  }

  public void deleteTrainerById(long trainerId) {
    trainerMap.remove(trainerId);
  }
}
