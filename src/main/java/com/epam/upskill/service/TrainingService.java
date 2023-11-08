package com.epam.upskill.service;

import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.dto.TrainersDtoList;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;

public interface TrainingService {
  Training findTrainingById(long trainingId);

  Training saveTraining(TrainingRequest trainingRequest);

  List<Training> findTrainingsByUsernameAndCriteria(String username, LocalDate periodFrom, LocalDate periodTo,
                                                    String trainerName, String trainingType);

  List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName);

  List<Training> findTrainerTrainings(String username, LocalDate periodFrom, LocalDate periodTo,
                                      String traineeName);

  List<Trainer> findNotAssignedActiveTrainersToTrainee(String username);

  List<TrainingType> findTrainingTypes();

  void delete(Training training);

  List<TrainerDtoForTrainee> updateTraineeTrainerList(String username, String trainingDate, String trainingName,
                                                      List<TrainersDtoList> list);
}
