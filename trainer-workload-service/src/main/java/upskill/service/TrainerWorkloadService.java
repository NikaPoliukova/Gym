package upskill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upskill.dao.TrainerWorkloadRepository;
import upskill.dto.TrainingRequestDto;
import upskill.entity.TrainerTraining;
import upskill.exception.IncorrectDateException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

  private final TrainerWorkloadRepository trainerWorkloadRepository;

  public TrainerTraining save(TrainerTraining trainerTraining) {
    return trainerWorkloadRepository.save(trainerTraining);
  }

  public Integer getTrainerWorkload(String trainerUsername, LocalDate periodFrom, LocalDate periodTo,
                                    String trainingType) {
    if (periodTo.isBefore(periodFrom)) {
      throw new IncorrectDateException("Incorrect period");
    }
    List<TrainerTraining> list = trainingType.isEmpty()
        ? trainerWorkloadRepository.findByTrainerUsernameAndTrainingDateBetween(trainerUsername, periodFrom, periodTo)
        : trainerWorkloadRepository.findByTrainerUsernameAndTrainingDateBetweenAndTrainingType(
        trainerUsername, periodFrom, periodTo, trainingType);
    return getSumOfTrainingDurations(list);
  }

  private int getSumOfTrainingDurations(List<TrainerTraining> trainingList) {
    return trainingList.stream()
        .mapToInt(TrainerTraining::getTrainingDuration)
        .sum();
  }

  public TrainerTraining getTrainerTraining(TrainingRequestDto trainingRequest) {
     return trainerWorkloadRepository.findByTrainerUsernameAndTrainingNameAndTrainingDateAndTrainingTypeAndTrainingDuration
        (trainingRequest.getTrainerUsername(), trainingRequest.getTrainingName(), trainingRequest.getTrainingDate(),
            trainingRequest.getTrainingType(), trainingRequest.getDuration());
  }

  public void delete(TrainingRequestDto trainingRequest) {
    var training = getTrainerTraining(trainingRequest);
    trainerWorkloadRepository.delete(training);
  }
}
