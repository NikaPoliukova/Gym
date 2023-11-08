package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends AbstractRepository<Trainer> {
  Optional<Trainer> findByUsername(String username);

  Optional<Trainer> update(Trainer trainer);

  void toggleProfileActivation(Trainer trainer);

  List<Trainer> findByIsActive();

  List<Trainee> findTraineesForTrainer(long id);

  Optional<Trainer> findByUsernameAndPassword(String username, String password);
}

