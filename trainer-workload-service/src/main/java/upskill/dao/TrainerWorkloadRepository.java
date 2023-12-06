package upskill.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import upskill.entity.TrainerTraining;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface TrainerWorkloadRepository extends JpaRepository<TrainerTraining, Long> {

  List<TrainerTraining> findByTrainerUsernameAndTrainingDateBetween(String trainerUsername,
                                                                    LocalDate periodFrom, LocalDate periodTo);

  List<TrainerTraining> findByTrainerUsernameAndTrainingDateBetweenAndTrainingType(String trainerUsername,
                                                                                   LocalDate periodFrom,
                                                                                   LocalDate periodTo,
                                                                                   String trainingType);

  TrainerTraining findByTrainerUsernameAndTrainingNameAndTrainingDateAndTrainingTypeAndTrainingDuration(
      String trainerUsername, String trainingName, LocalDate trainingDate, String trainingType, int trainingDuration
  );
}


