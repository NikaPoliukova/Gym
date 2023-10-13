package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainee;

public interface TraineeRepository extends AbstractRepository<Trainee> {

  void updateTrainee(Trainee trainee);

  void deleteTraineeById(long traineeId);
}
