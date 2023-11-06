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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.epam.upskill.util.UserUtils.getLocalDate;

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
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String periodFrom, String periodTo, String trainerName,
                                                           TrainingTypeEnum myEnum) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get("trainee").get("username"), username));
    if (trainerName != null && !trainerName.isEmpty()) {
      predicates.add(cb.equal(trainingRoot.get("trainer").get("username"), trainerName));
    }

    if (myEnum != null) {
      predicates.add(cb.equal(trainingRoot.get("trainingType").get("trainingTypeName"), myEnum));
    }

    if (periodFrom != null && !periodFrom.isEmpty() && periodTo != null && !periodTo.isEmpty()) {
      LocalDate fromDate = LocalDate.parse(periodFrom);
      LocalDate toDate = LocalDate.parse(periodTo);
      predicates.add(cb.between(trainingRoot.get("trainingDate"), fromDate, toDate));
    }
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName) {
    LocalDate date = getLocalDate(trainingDate);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get("trainee").get("id"), traineeId));
    predicates.add(cb.equal(trainingRoot.get("trainingDate"), date));
    predicates.add(cb.equal(trainingRoot.get("trainingName"), trainingName));
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();

  }

  @Override
  public List<Training> findTrainerTrainings(long trainerId, String periodFrom, String periodTo, String traineeName) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get("trainer").get("id"), trainerId));
    if (traineeName != null && !traineeName.isEmpty()) {
      predicates.add(cb.equal(trainingRoot.get("trainee").get("username"), traineeName));
    }
    if (periodFrom != null && !periodFrom.isEmpty() && periodTo != null && !periodTo.isEmpty()) {
      LocalDate fromDate = LocalDate.parse(periodFrom);
      LocalDate toDate = LocalDate.parse(periodTo);
      predicates.add(cb.between(trainingRoot.get("trainingDate"), fromDate, toDate));
    }
     query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
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

  @Override
  public List<Trainer> getAssignedActiveTrainersToTrainee(long traineeId) {
    TypedQuery<Trainer> query = entityManager.createQuery(
        "SELECT DISTINCT t.trainer FROM Training t  WHERE t.trainee.id = :traineeId ", Trainer.class);
    query.setParameter("traineeId", traineeId);
    return query.getResultList();
  }

  @Override
  public List<TrainingType> findTrainingTypes() {
    return entityManager.createQuery("SELECT tt FROM TrainingType tt ", TrainingType.class).getResultList();
  }

  @Override
  public void delete(Training training) {
    log.debug("Deleting user by ID: " + training);
    entityManager.remove(training);
  }
}

