package com.epam.upskill.dao;

import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends AbstractRepository<Trainee> {

  Optional<Trainee> findByUsername(String username);

  void toggleProfileActivation(Trainee trainee);

  Optional<Trainee> update(Trainee trainee);

  List<Trainer> findTrainersForTrainee(long id);

  Optional<Trainee> findByUsernameAndPassword(String username, String password);
}
