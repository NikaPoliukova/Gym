package upskill.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import upskill.dao.TrainingRepository;
import upskill.dto.*;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.entity.TrainingType;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static upskill.util.UserUtils.getLocalDate;

@Slf4j
@Repository
public class TrainingRepositoryImpl implements TrainingRepository {
  public static final String USERNAME = "username";
  public static final String TRAINEE = "trainee";
  public static final String TRAINING_DATE = "trainingDate";
  public static final String ID = "id";
  public static final String TRAINER = "trainer";

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
    return entityManager.createQuery("SELECT e FROM Training e", Training.class).getResultList();
  }


  @Override
  public List<Training> findTraineeTrainingsList(TrainingDtoRequest request) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = getPredicates(request, cb, trainingRoot);
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }

  @Override
  public List<Training> findTraineeTrainingsList(String username, LocalDate periodFrom, LocalDate periodTo,
                                                 String trainerName) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = getPredicates(username, periodFrom, periodTo, trainerName, cb, trainingRoot);
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }

  @Override
  public List<Training> findTraineeTrainingsList(long traineeId, String trainingDate, String trainingName) {
    LocalDate date = getLocalDate(trainingDate);
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = getPredicates(traineeId, trainingName, date, cb, trainingRoot);
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }

  @Override
  public List<Training> findTrainerTrainings(TrainingTrainerDto dto) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    List<Predicate> predicates = getPredicates(dto, cb, trainingRoot);
    query.select(trainingRoot).where(predicates.toArray(new Predicate[0]));
    TypedQuery<Training> typedQuery = entityManager.createQuery(query);
    return typedQuery.getResultList();
  }


  @Override
  public TrainingType findTrainingTypeByName(String name) {
    try {
      var trainingType = TrainingTypeEnum.valueOf(name);
      TypedQuery<TrainingType> query = entityManager.createQuery(
          "SELECT tt FROM TrainingType tt WHERE tt.trainingTypeName = :name", TrainingType.class);
      query.setParameter("name", trainingType);
      return query.getSingleResult();
    } catch (InvalidDataAccessApiUsageException e) {
      throw new InvalidDataAccessApiUsageException(name);
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


  @Override
  public Optional<Training> findTraining(TrainingRequest trainingRequest) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    Predicate predicate = cb.and(
        cb.equal(trainingRoot.get("traineeUsername"), trainingRequest.traineeUsername()),
        cb.equal(trainingRoot.get("trainerUsername"), trainingRequest.trainerUsername()),
        cb.equal(trainingRoot.get("trainingName"), trainingRequest.trainingName()),
        cb.equal(trainingRoot.get("trainingDate"), trainingRequest.trainingDate()),
        cb.equal(trainingRoot.get("trainingType"), trainingRequest.trainingType()),
        cb.equal(trainingRoot.get("trainingDuration"), trainingRequest.trainingDuration())
    );

    query.select(trainingRoot).where(predicate);
    try {
      return Optional.of(entityManager.createQuery(query).getSingleResult());
    } catch (NoResultException | NonUniqueResultException e) {
      log.debug("trainee was not found");
      return Optional.empty();
    }
  }

  @Override
  public Optional<Training> findTraining(TrainingRequestDto trainingRequest) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Training> query = cb.createQuery(Training.class);
    Root<Training> trainingRoot = query.from(Training.class);
    Predicate predicate = cb.and(
        cb.equal(trainingRoot.get("trainerUsername"), trainingRequest.getTrainerUsername()),
        cb.equal(trainingRoot.get("trainingName"), trainingRequest.getTrainingName()),
        cb.equal(trainingRoot.get("trainingDate"), trainingRequest.getTrainingDate()),
        cb.equal(trainingRoot.get("trainingType").get("name"), trainingRequest.getTrainingType()),
        cb.equal(trainingRoot.get("trainingDuration"), trainingRequest.getDuration())
    );

    query.select(trainingRoot).where(predicate);
    try {
      return Optional.of(entityManager.createQuery(query).getSingleResult());
    } catch (NoResultException | NonUniqueResultException e) {
      log.debug("trainee was not found");
      return Optional.empty();
    }
  }

  @Override
  public void delete(Trainer trainer, TrainingType trainingType, String trainingName,
                     int duration, LocalDate trainingDate) {
    String jpql = "DELETE FROM Training t WHERE " +
        "t.trainer = :trainer " +
        "AND t.trainingName = :trainingName " +
        "AND t.trainingDate = :trainingDate " +
        "AND t.trainingType = :trainingType " +
        "AND t.trainingDuration = :duration";

    entityManager.createQuery(jpql)
        .setParameter("trainer", trainer)
        .setParameter("trainingName", trainingName)
        .setParameter("trainingDate", trainingDate)
        .setParameter("trainingType", trainingType)
        .setParameter("duration", duration)
        .executeUpdate();
    }

  private static List<Predicate> getPredicates(TrainingDtoRequest request, CriteriaBuilder cb, Root<Training> trainingRoot) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get(TRAINEE).get(USERNAME), request.username()));
    if (request.trainerName() != null) {
      predicates.add(cb.equal(trainingRoot.get(TRAINER).get(USERNAME), request.trainerName()));
    }
    if (request.trainingType() != null) {
      predicates.add(cb.equal(trainingRoot.get("trainingType").get("trainingTypeName"), request.trainingType()));
    }
    if (request.periodFrom() != null && request.periodTo() != null) {
      predicates.add(cb.between(trainingRoot.get(TRAINING_DATE), request.periodFrom(), request.periodTo()));
    }
    return predicates;
  }

  private static List<Predicate> getPredicates(long traineeId, String trainingName, LocalDate date, CriteriaBuilder cb, Root<Training> trainingRoot) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get(TRAINEE).get(ID), traineeId));
    predicates.add(cb.equal(trainingRoot.get(TRAINING_DATE), date));
    predicates.add(cb.equal(trainingRoot.get("trainingName"), trainingName));
    return predicates;
  }

  private List<Predicate> getPredicates(String username, LocalDate periodFrom, LocalDate periodTo,
                                        String trainerName, CriteriaBuilder cb, Root<Training> trainingRoot) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get(TRAINEE).get(USERNAME), username));
    if (periodFrom != null && periodTo != null) {
      predicates.add(cb.between(trainingRoot.get(TRAINING_DATE), periodFrom, periodTo));
    }
    if (trainerName != null && !trainerName.isEmpty()) {
      predicates.add(cb.equal(trainingRoot.get(TRAINER).get(USERNAME), trainerName));
    }
    return predicates;
  }

  private static List<Predicate> getPredicates(TrainingTrainerDto dto, CriteriaBuilder cb, Root<Training> trainingRoot) {
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(cb.equal(trainingRoot.get(TRAINER).get(ID), dto.trainerId()));
    if (dto.traineeName() != null && !dto.traineeName().isEmpty()) {
      predicates.add(cb.equal(trainingRoot.get(TRAINEE).get(USERNAME), dto.traineeName()));
    }
    if (dto.periodFrom() != null && dto.periodTo() != null) {
      predicates.add(cb.between(trainingRoot.get(TRAINING_DATE), dto.periodFrom(), dto.periodTo()));
    }
    return predicates;
  }
}

