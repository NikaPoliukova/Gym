package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;
import java.util.Set;

public interface TrainingRepository extends AbstractRepository<Training> {

  TrainingType findTrainingTypeById(int id);

  List<Training> findTrainingsByUsernameAndCriteria(String trainerUsername, String trainingName);

  List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId);
}
