package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {
  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Training save(Training training) {
    log.debug("Creating Training: " + training);
    entityManager.persist(training);
    return training;
  }

  @Override
  public Optional<Training> findById(long id) {
    log.debug("Finding Training by ID: " + id);
    return Optional.ofNullable(entityManager.find(Training.class, id));
  }

  @Override
  public List<Training> findAll() {
    log.debug("Fetching all Trainings");
    return entityManager.createQuery("SELECT e FROM training e", Training.class).getResultList();
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String trainingName) {
    String sql = "SELECT t FROM Training t WHERE t.trainee.username = :username OR t.trainer.username = :username";
    if (!trainingName.isEmpty()) {
      sql += " AND t.trainingName LIKE :trainingName";
    }
    TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
    query.setParameter("username", username);
    if (!trainingName.isEmpty()) {
      query.setParameter("trainingName", "%" + trainingName + "%");
    }
    return query.getResultList();
  }

  @Override
  public TrainingType findTrainingTypeById(int id) {
    TypedQuery<TrainingType> query = entityManager.createQuery(
        "SELECT tt FROM TrainingType tt WHERE tt.id = :id", TrainingType.class);
    query.setParameter("id", id);

    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      throw new EntityNotFoundException("TrainingType not found with id: " + id);
    }
  }

  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    TypedQuery<Trainer> query = entityManager.createQuery(
        "SELECT DISTINCT t.trainer FROM Training t  WHERE t.trainee.id = :traineeId " +
            "AND t.trainer.isActive = false ", Trainer.class);
    query.setParameter("traineeId", traineeId);
    return query.getResultList();
  }
}

