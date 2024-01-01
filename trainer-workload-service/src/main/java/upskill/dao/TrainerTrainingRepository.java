package upskill.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import upskill.entity.TrainingTrainerSummary;

@Repository
public interface TrainerTrainingRepository extends MongoRepository<TrainingTrainerSummary, String> {
}
