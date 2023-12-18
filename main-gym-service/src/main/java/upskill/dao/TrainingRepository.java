package upskill.dao;

import upskill.dto.TrainingDtoRequest;
import upskill.dto.TrainingRequest;
import upskill.dto.TrainingRequestDto;
import upskill.dto.TrainingTrainerDto;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.entity.TrainingType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

  Optional<Training> findTraining(TrainingRequest trainingRequest);

  Optional<Training> findTraining(TrainingRequestDto trainingRequest);

  void delete(Trainer trainer, TrainingType traininigType, String trainingName, int duration, LocalDate trainingDate);
}
