package com.epam.upskill.dao;

import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;

public interface TrainingRepository extends AbstractRepository<Training> {

  TrainingType findTrainingTypeById(int id);

  TrainingType findTrainingTypeByName(String name);

  List<Training> findTrainingsByUsernameAndCriteria(String username, String password, String periodFrom, String periodTo,
                                                    String trainerName, String trainingType);

  List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId);

  List<TrainingType> findTrainingTypes();
}
