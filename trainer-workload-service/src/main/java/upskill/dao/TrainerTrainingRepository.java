package upskill.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import upskill.entity.TrainingTrainerSummary;

import java.util.Optional;


public interface TrainerTrainingRepository extends MongoRepository<TrainingTrainerSummary, String> {

  Optional<TrainingTrainerSummary> findByUsername(String username);

  @Query(value = "{'username': :trainerUsername, 'yearsList': { '$elemMatch': { 'year': :year, " +
      "'months': { '$elemMatch': { 'month': :month } } } } }", fields = "{'yearsList.months.duration': 1}")
  Optional<Integer> getTrainerWorkload(@Param("trainerUsername") String trainerUsername, @Param("year") int year,
                                       @Param("month") int month
  );

  @Query("{'username': ?0, 'yearsList.year': ?1, 'yearsList.monthsList.monthValue': ?2}")
  Optional<TrainingTrainerSummary> findByUsernameAndYearAndMonth(String username, int year, int monthValue);
}
