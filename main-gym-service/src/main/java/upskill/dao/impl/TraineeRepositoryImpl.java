package upskill.dao.impl;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import upskill.dao.TraineeRepository;
import upskill.entity.Trainee;
import upskill.entity.Trainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
@NoArgsConstructor
@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

  public static final String USERNAME = "username";

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
    try {
      return Optional.of(entityManager.createQuery("SELECT t FROM Trainee t  WHERE t.username = :username",
              Trainee.class)
          .setParameter(USERNAME, username)
          .getSingleResult());
    } catch (Exception ex) {
      log.debug("trainee was not found");
      return Optional.empty();
    }
  }

  @Override
  public Optional<Trainee> findByUsernameAndPassword(String username, String password) {
    return Optional.of(entityManager.createQuery("SELECT t FROM Trainee t  WHERE " +
            "t.username = :username AND t.password = :password", Trainee.class)
        .setParameter(USERNAME, username)
        .setParameter("password", password)
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

  @Override
  public List<Trainer> findTrainersForTrainee(long traineeId) {
    TypedQuery<Trainer> query = entityManager.createQuery(
        "SELECT DISTINCT t.trainer FROM Training t WHERE t.trainee.id = :traineeId", Trainer.class);
    query.setParameter("traineeId", traineeId);
    return query.getResultList();
  }
}

