package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainee;

public interface TraineeRepository extends AbstractRepository<Trainee> {

  Trainee findByUsername(String username);

  void update(Trainee trainee);

  void delete(long traineeId);

}
