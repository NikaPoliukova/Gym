package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Training;

import java.util.List;

public interface TraineeRepository extends AbstractRepository<Trainee> {

  Trainee findByUsername(String username);

  void toggleProfileActivation(Trainee trainee);

  Trainee update(Trainee trainee);

  void delete(long traineeId);
}
