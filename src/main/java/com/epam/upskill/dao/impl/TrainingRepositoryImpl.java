package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {
  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public void save(Training training) {
    log.debug("Creating Training: " + training);
    entityManager.persist(training);
  }

  @Override
  public Training findById(long id) {
    log.debug("Finding Training by ID: " + id);
    return entityManager.find(Training.class, id);
  }

  @Override
  public List<Training> findAll() {
    log.debug("Fetching all Trainings");
    return entityManager.createQuery("SELECT e FROM training e", Training.class).getResultList();
  }

  public List<Training> findTrainingsByUsernameAndCriteria(String username, String criteria) {
    String sql = "SELECT t FROM Training t WHERE (t.trainee.user.username = :username OR t.trainer.username = :username) ";
    if (!criteria.isEmpty()) {
      sql += "AND (t.trainingName LIKE :trainingName OR t.trainer.specialization " +
          "LIKE :specialization OR t.trainee.address LIKE :address)";
    }

    TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
    query.setParameter("username", username);

    if (!criteria.isEmpty()) {
      query.setParameter("trainingName", "%" + criteria + "%");
      query.setParameter("specialization", "%" + criteria + "%");
      query.setParameter("address", "%" + criteria + "%");
    }
    return query.getResultList();
  }
  @Override
  public TrainingType findTrainingTypeById(long id) {
    log.debug("Finding Training Type by ID: " + id);
    return entityManager.find(TrainingType.class, id);
  }
}

