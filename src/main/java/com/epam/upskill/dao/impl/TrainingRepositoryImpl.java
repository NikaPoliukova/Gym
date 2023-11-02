package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

  //TODO обновить и добавить поля
  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String periodFrom, String periodTo,
                                                           String trainerName, String trainingType) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> training = query.from(Training.class);
    Predicate traineePredicate = cb.equal(training.get("trainee").get("username"), username);
    Predicate trainerPredicate = cb.equal(training.get("trainer").get("username"), username);
    Predicate usernamePredicate = cb.or(traineePredicate, trainerPredicate);
    query.where(usernamePredicate);
//    if (!trainingName.isEmpty()) {
//      Predicate trainingNamePredicate = cb.like(training.get("trainingName"), "%" + trainingName + "%");
//      query.where(cb.and(usernamePredicate, trainingNamePredicate));
//    }
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
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

  @Override
  public TrainingType findTrainingTypeByName(String name) {
    TypedQuery<TrainingType> query = entityManager.createQuery(
        "SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :name", TrainingType.class);
    query.setParameter("name", TrainingTypeEnum.valueOf(name));
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      throw new EntityNotFoundException("TrainingType not found with name: " + name);
    }
  }

  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    TypedQuery<Trainer> query = entityManager.createQuery(
        "SELECT DISTINCT t.trainer FROM Training t  WHERE t.trainee.id = :traineeId " +
            "AND t.trainer.isActive = false ", Trainer.class);
    query.setParameter("traineeId", traineeId);
    return query.getResultList();
  }

  @Override
  public List<TrainingType> findTrainingTypes() {
    return entityManager.createQuery("SELECT tt FROM TrainingType tt ", TrainingType.class).getResultList();

  }
}

