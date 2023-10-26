package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.entity.Trainee;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public Trainee save(Trainee trainee) {
    log.debug("Creating Trainee: " + trainee);
    entityManager.persist(trainee);
    return trainee;
  }

  @Override
  public Optional<Trainee> findByUsername(String username) {
    return Optional.of(entityManager.createQuery("SELECT t FROM Trainee t  WHERE t.username = :username",
            Trainee.class)
        .setParameter("username", username)
        .getSingleResult());
  }

  @Override
  public Optional<Trainee> update(Trainee trainee) {
    log.debug("Updating Trainee: " + trainee);
    return Optional.of(entityManager.merge(trainee));
  }

  @Override
  public Optional<Trainee> findById(long id) {
    log.debug("Finding Trainee by ID: " + id);
    return Optional.ofNullable(entityManager.find(Trainee.class, id));
  }

  @Override
  public List<Trainee> findAll() {
    log.debug("Fetching all Trainees");
    return entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class).getResultList();
  }

  @Override
  public void toggleProfileActivation(Trainee trainee) {
    log.debug("Toggling Trainee profile activation: " + trainee);
    entityManager.merge(trainee);
  }
}

