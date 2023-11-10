package com.epam.upskill.dao;

import com.epam.upskill.dto.TrainingDtoRequest;
import com.epam.upskill.dto.TrainingTrainerDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends AbstractRepository<Training> {

  TrainingType findTrainingTypeByName(String name);

  List<Training> findTraineeTrainingsList(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName);

  List<Training> findTraineeTrainingsList(TrainingDtoRequest request);

  List<Training> findTraineeTrainingsList(long traineeId, String trainingDate, String trainingName);

  List<Training> findTrainerTrainings(TrainingTrainerDto dto);

  List<Trainer> getAssignedActiveTrainersToTrainee(long traineeId);

  List<TrainingType> findTrainingTypes();

  List<Training> findAll();

  void delete(Training training);
}
