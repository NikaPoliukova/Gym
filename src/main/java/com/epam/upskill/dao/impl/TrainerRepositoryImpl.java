package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

  public static final String USERNAME = "username";

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Trainer save(Trainer trainer) {
    log.debug("Creating Trainer: " + trainer);
    entityManager.persist(trainer);
    return trainer;
  }

  @Override
  public List<Trainer> findByIsActive() {
    String jpql = "SELECT t FROM Trainer t  WHERE t.isActive = :isActive";
    TypedQuery<Trainer> query = entityManager.createQuery(jpql, Trainer.class);
    query.setParameter("isActive", true);
    return query.getResultList();
  }

  @Override
  public List<Trainee> findTraineesForTrainer(long trainerId) {
    TypedQuery<Trainee> query = entityManager.createQuery(
        "SELECT DISTINCT t.trainee FROM Training t WHERE t.trainer.id = :trainerId", Trainee.class);
    query.setParameter("trainerId", trainerId);
    return query.getResultList();
  }

  @Override
  public Optional<Trainer> findById(long id) {
    log.debug("Finding Trainer by ID: " + id);
    return Optional.ofNullable(entityManager.find(Trainer.class, id));
  }

  @Override
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    return entityManager.createQuery("SELECT e FROM Trainer e", Trainer.class).getResultList();
  }

  @Override
  public void toggleProfileActivation(Trainer trainer) {
    log.debug("Toggling profile activation for Trainer: " + trainer);
    entityManager.merge(trainer);
  }

  @Override
  public Optional<Trainer> findByUsername(String username) {
    try {
      return Optional.ofNullable(entityManager.createQuery("SELECT t FROM Trainer t  WHERE t.username = :username",
              Trainer.class)
          .setParameter(USERNAME, username)
          .getSingleResult());
    } catch (NoResultException ex) {
      log.debug("trainer was not found");
      return Optional.empty();
    }
  }

  @Override
  public Optional<Trainer> findByUsernameAndPassword(String username, String password) {
    return Optional.of(entityManager.createQuery("SELECT t FROM Trainer t  WHERE " +
            "t.username = :username AND t.password = :password", Trainer.class)
        .setParameter(USERNAME, username)
        .setParameter("password", password)
        .getSingleResult());
  }

  @Override
  public Optional<Trainer> update(Trainer trainer) {
    log.debug("Updating Trainer: " + trainer);
    return Optional.of(entityManager.merge(trainer));
  }
}

