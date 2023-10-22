package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainer;

import java.util.List;

public interface TrainerRepository extends AbstractRepository<Trainer> {
  Trainer findByUsername(String username);

  void update(Trainer trainer);

  void delete(long trainerId);

  List<Trainer> findByIsActive(boolean isActive);
}
