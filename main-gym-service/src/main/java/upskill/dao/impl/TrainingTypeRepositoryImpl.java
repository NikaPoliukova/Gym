package upskill.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import upskill.dao.TrainingTypeRepository;
import upskill.entity.TrainingType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {
  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public TrainingType save(TrainingType trainingType) {
    log.debug("Creating TrainingType: " + trainingType);
    entityManager.persist(trainingType);
    return trainingType;
  }

}
