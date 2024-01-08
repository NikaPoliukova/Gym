package upskill.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import upskill.entity.TrainingTrainerSummary;

import java.util.Optional;


public interface TrainerTrainingRepository extends MongoRepository<TrainingTrainerSummary, String> {

  Optional<TrainingTrainerSummary> findByUsername(String username);

  @Query("{'username': ?0, 'yearsList.year': ?1, 'yearsList.monthsList.monthValue': ?2}")
  Optional<TrainingTrainerSummary> findByUsernameAndYearAndMonth(String username, int year, int month);

}
