package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainer;

public interface TrainerRepository extends AbstractRepository<Trainer> {
  void updateTrainer(Trainer trainer);

  void deleteTrainerById(long trainerId);
}
