package com.epam.upskill.storage;

import com.epam.upskill.entity.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TrainerStorage {
  private static final Logger logger = LoggerFactory.getLogger(TrainerStorage.class);

  private Map<Long, Trainer> trainerMap = new HashMap<>();

  public Map<Long, Trainer> getTrainerMap() {
    return trainerMap;
  }

  public void save(Trainer trainer) {
    trainerMap.put(trainer.getId(), trainer);
    logger.debug("Trainer saved: " + trainer);
  }

  public Trainer findById(long id) {
    Trainer trainer = trainerMap.get(id);
    if (trainer != null) {
      logger.debug("Found Trainer by ID: " + id);
    } else {
      logger.warn("Trainer not found for ID: " + id);
    }
    return trainer;
  }

  public void updateTrainer(Trainer updatedTrainer) {
    long id = updatedTrainer.getId();
    if (trainerMap.containsKey(id)) {
      trainerMap.put(id, updatedTrainer);
      logger.debug("Trainer updated: " + updatedTrainer);
    } else {
      logger.warn("Trainer not found for update: ID " + id);
    }
  }

  public void deleteTrainerById(long trainerId) {
    if (trainerMap.containsKey(trainerId)) {
      trainerMap.remove(trainerId);
      logger.debug("Trainer deleted: ID " + trainerId);
    } else {
      logger.warn("Trainer not found for delete: ID " + trainerId);
    }
  }
}

