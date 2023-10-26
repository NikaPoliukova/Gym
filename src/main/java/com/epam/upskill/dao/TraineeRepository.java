package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainee;

import java.util.Optional;

public interface TraineeRepository extends AbstractRepository<Trainee> {

  Optional<Trainee> findByUsername(String username);

  void toggleProfileActivation(Trainee trainee);

  Optional<Trainee> update(Trainee trainee);
}
