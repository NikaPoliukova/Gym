package com.epam.upskill.dao;

import com.epam.upskill.entity.Training;

import java.util.List;

public interface TrainingRepository extends AbstractRepository<Training> {

  List<Training> findTrainingsByUsernameAndCriteria(String trainerUsername, String trainingName);
  }
