package upskill.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import upskill.entity.TrainingTrainerSummary;

import java.util.List;

@Repository
public interface TrainerTrainingRepository extends MongoRepository<TrainingTrainerSummary, String> {
  List<TrainingTrainerSummary> findByUsernameAndStatus(String username, boolean status);
}
