package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.entity.Trainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Slf4j
@Repository
public class TrainerRepositoryImpl implements TrainerRepository {
  @PersistenceContext
  private EntityManager entityManager;


  @Override
  public void save(Trainer trainer) {
    log.debug("Creating Trainer: " + trainer);
    entityManager.persist(trainer);
 }

  @Override
  public List<Trainer> findByIsActive() {
    String jpql = "SELECT t FROM Trainer t  WHERE t.isActive = :isActive";
    TypedQuery<Trainer> query = entityManager.createQuery(jpql, Trainer.class);
    query.setParameter("isActive", true);
    return query.getResultList();
  }

  @Override
  public Trainer findById(long id) {
    log.debug("Finding Trainer by ID: " + id);
    return entityManager.find(Trainer.class, id);
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
  public Trainer findByUsername(String username) {
    return entityManager.createQuery("SELECT t FROM Trainer t  WHERE t.username = :username",
            Trainer.class)
        .setParameter("username", username)
        .getSingleResult();
  }

  @Override
  public Trainer update(Trainer trainer) {
    log.debug("Updating Trainer: " + trainer);
    return entityManager.merge(trainer);
  }

  @Override
  public void delete(long trainerId) {
    log.debug("Deleting Trainer by ID: " + trainerId);
    Trainer entity = findById(trainerId);
    if (entity != null) {
      entityManager.remove(entity);
    }
  }
}

