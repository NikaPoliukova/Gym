package com.epam.upskill.dao.impl;

import com.epam.upskill.dao.TrainingTypeRepository;
import com.epam.upskill.entity.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
