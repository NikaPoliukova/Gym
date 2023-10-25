package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainer;

import java.util.List;

public interface TrainerRepository extends AbstractRepository<Trainer> {
  Trainer findByUsername(String username);

  Trainer update(Trainer trainer);

  void delete(long trainerId);
  void toggleProfileActivation(Trainer trainer);
  List<Trainer> findByIsActive();
}
