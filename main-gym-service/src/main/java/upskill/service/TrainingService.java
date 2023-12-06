package upskill.service;

import upskill.dto.*;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.entity.TrainingType;

import java.util.List;

public interface TrainingService {
  Training findTrainingById(long trainingId);

  Training saveTraining(TrainingRequest trainingRequest);

  List<Training> findTrainingsByUsernameAndCriteria(TrainingTraineeRequest request);

  List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName);

  List<Training> findTrainerTrainings(TrainingTrainerRequest request);

  List<Trainer> findNotAssignedActiveTrainersToTrainee(String username);

  List<TrainingType> findTrainingTypes();

  void delete(Training training);

  List<TrainerDtoForTrainee> updateTraineeTrainerList(UpdateTraineeTrainerDto dto);

  void delete(TrainingRequestDto trainingRequest) ;

  Training findTraining(TrainingRequest trainingRequest);
}
