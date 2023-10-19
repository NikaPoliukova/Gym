package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TrainerStorage {
  private Map<Long, Trainer> trainerMap = new HashMap<>();

  public Map<Long, Trainer> getTrainerMap() {
    return Collections.unmodifiableMap(trainerMap);
  }

  public void save(Trainer trainer) {
    trainerMap.put(trainer.getId(), trainer);
    log.debug("Trainer saved: " + trainer);
  }

  public Trainer findById(long id) {
    Trainer trainer = trainerMap.get(id);
    if (trainer != null) {
      log.debug("Found Trainer by ID: " + id);
    } else {
      log.warn("Trainer not found for ID: " + id);
    }
    return trainer;
  }

  public void updateTrainer(Trainer updatedTrainer) {
    long id = updatedTrainer.getId();
    if (trainerMap.containsKey(id)) {
      trainerMap.put(id, updatedTrainer);
      log.debug("Trainer updated: " + updatedTrainer);
    } else {
      log.warn("Trainer not found for update: ID " + id);
    }
  }

  public void deleteTrainerById(long trainerId) {
    if (trainerMap.containsKey(trainerId)) {
      trainerMap.remove(trainerId);
      log.debug("Trainer deleted: ID " + trainerId);
    } else {
      log.warn("Trainer not found for delete: ID " + trainerId);
    }
  }
}

