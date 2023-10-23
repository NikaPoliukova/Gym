package com.epam.upskill.dao;

import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;

public interface TrainingRepository extends AbstractRepository<Training> {
  TrainingType findTrainingTypeById(long id);
  List<Training> findTrainingsByUsernameAndCriteria(String trainerUsername, String trainingName);
}
