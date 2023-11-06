package com.epam.upskill.dao;

import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.util.List;

public interface TrainingRepository extends AbstractRepository<Training> {

  TrainingType findTrainingTypeById(int id);

  TrainingType findTrainingTypeByName(String name);

  List<Training> findTrainingsByUsernameAndCriteria(String username,  String periodFrom, String periodTo,
                                                    String trainerName,  TrainingTypeEnum myEnum);
  List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName);
  List<Trainer> getAssignedActiveTrainersToTrainee(long traineeId);

  List<TrainingType> findTrainingTypes();

  List<Training> findAll();

  void delete(Training training);
}
