package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.entity.Trainee;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@NoArgsConstructor
@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public void save(Trainee trainee) {
    log.debug("Creating Trainee: " + trainee);
    entityManager.persist(trainee);
  }

  @Override
  public Trainee findByUsername(String username) {
    return entityManager.createQuery("SELECT e FROM trainee e WHERE e.username = :username", Trainee.class)
        .setParameter("username", username)
        .getSingleResult();
  }

  @Override
  public void update(Trainee trainee) {
    log.debug("Updating Trainee: " + trainee);
    entityManager.merge(trainee);
  }

  @Override
  public Trainee findById(long id) {
    log.debug("Finding Trainee by ID: " + id);
    return entityManager.find(Trainee.class, id);
  }

  @Override
  public List<Trainee> findAll() {
    log.debug("Fetching all Trainees");
    return entityManager.createQuery("SELECT e FROM trainee e", Trainee.class).getResultList();
  }


  @Override
  public void delete(long traineeId) {
    log.debug("Deleting Trainee by ID: " + traineeId);
    Trainee entity = findById(traineeId);
    if (entity != null) {
      entityManager.remove(entity);
    }
  }
}

