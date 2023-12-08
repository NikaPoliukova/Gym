package upskill.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upskill.dao.TrainerWorkloadRepository;
import upskill.entity.TrainerTraining;
import upskill.exception.IncorrectDateException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerWorkloadService {

  private final TrainerWorkloadRepository trainerWorkloadRepository;

  public TrainerTraining save(TrainerTraining trainingRequest) {
    return trainerWorkloadRepository.save(trainingRequest);
  }


  public Integer getTrainerWorkload(String trainerUsername, LocalDate periodFrom, LocalDate periodTo,
                                    String trainingType) {
    if (periodTo.isAfter(periodFrom)) {
      if (trainingType.isEmpty()) {
        List<TrainerTraining> list =
            trainerWorkloadRepository.findByTrainerUsernameAndTrainingDateBetween(trainerUsername, periodFrom, periodTo);
        return getSumOfTrainingDurations(list);
      } else {
        List<TrainerTraining> listByType =
            trainerWorkloadRepository.findByTrainerUsernameAndTrainingDateBetweenAndTrainingType(trainerUsername,
                periodFrom, periodTo, trainingType);
        return getSumOfTrainingDurations(listByType);
      }
    } else throw new IncorrectDateException("incorrect period");
  }

  private int getSumOfTrainingDurations(List<TrainerTraining> trainingList) {
    return trainingList.stream()
        .mapToInt(TrainerTraining::getTrainingDuration)
        .sum();
  }

  public TrainerTraining getTrainerTraining(TrainerTraining trainingRequest) {
    return
        trainerWorkloadRepository.findByTrainerUsernameAndTrainingNameAndTrainingDateAndTrainingTypeAndTrainingDuration
            (trainingRequest.getTrainerUsername(), trainingRequest.getTrainingName(), trainingRequest.getTrainingDate(),
                trainingRequest.getTrainingType(), trainingRequest.getTrainingDuration());
  }

  public void delete(TrainerTraining trainingRequest) {
    TrainerTraining training = getTrainerTraining(trainingRequest);
    trainerWorkloadRepository.delete(training);
  }
}
