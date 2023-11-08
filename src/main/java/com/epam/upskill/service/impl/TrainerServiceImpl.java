package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TraineeDtoForTrainer;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.dto.TrainerUpdateRequest;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.epam.upskill.service.impl.TraineeServiceImpl.TRANSACTION_ID;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

  private final TrainerRepository trainerRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;
  private final TrainingRepository trainingRepository;

  @Override
  @Transactional
  public Trainer findById(long trainerId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching Trainer by ID: {}", transactionId, trainerId);
    return trainerRepository.findById(trainerId).orElseThrow(()
        -> new UserNotFoundException("trainer with id = " + trainerId));
  }

  @Override
  @Transactional(readOnly = true)
  public Trainer findByUsername(String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching Trainer by username: {}", transactionId, username);
    return trainerRepository.findByUsername(username).orElseThrow(()
        -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public Trainer findByUsernameAndPassword(String username, String password) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching with username: {} and password = {}", transactionId, username, password);
    return trainerRepository.findByUsernameAndPassword(username, password).orElseThrow(()
        -> new UserNotFoundException(username));

  }

  @Override
  @Transactional
  public List<Trainer> findAll() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.debug("Transaction ID: {} | Fetching all Trainers", transactionId);
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override
  @Transactional
  public Trainer saveTrainer(TrainerRegistration trainerRegistration) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Creating Trainer from TrainerRegistration: {}", transactionId, trainerRegistration);
    var username = UserUtils.createUsername(trainerRegistration.firstName(), trainerRegistration.lastName(),
        userService.findAll());
    var password = UserUtils.generateRandomPassword();
    Trainer trainer = new Trainer();
    TrainingType trainingType = trainingRepository.findTrainingTypeByName(trainerRegistration.specialization());
    fillInTheTrainer(trainerRegistration, username, password, trainer, trainingType);
    return trainerRepository.save(trainer);
  }

  @Override
  @Transactional
  public Trainer update(TrainerUpdateRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Updating Trainer with TrainerDto: {}", transactionId, request);
    var trainer = findByUsername(request.username());
    Optional.ofNullable(request.firstName()).filter(firstName -> !firstName.isEmpty())
        .ifPresent(trainer::setFirstName);
    if (request.isActive() != trainer.isActive()) {
      trainer.setActive(request.isActive());
    }
    return trainerRepository.update(trainer).get();
  }

  @Override
  @Transactional
  public List<Trainer> findByIsActive() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching active Trainers", transactionId);
    List<Trainer> activeTrainers = trainerRepository.findByIsActive();
    return activeTrainers.isEmpty() ? Collections.emptyList() : activeTrainers;

  }

  @Override
  @Transactional
  public void toggleProfileActivation(long trainerId, boolean isActive) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Toggling profile activation for Trainer with ID: {}", transactionId, trainerId);
    var trainer = findById(trainerId);
    trainer.setActive(isActive);
    trainerRepository.toggleProfileActivation(trainer);

  }

  @Override
  @Transactional
  public List<TraineeDtoForTrainer> findTraineesForTrainer(long id) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching Trainees for Trainer with ID: {}", transactionId, id);
    List<Trainee> listTrainees = trainerRepository.findTraineesForTrainer(id);
    if (listTrainees.isEmpty()) {
      return Collections.emptyList();
    } else {
      return traineeConverter.toTraineeDtoForTrainer(listTrainees);
    }
  }

  private static void fillInTheTrainer(TrainerRegistration trainerRegistration, String username, String password,
                                       Trainer trainer, TrainingType trainingType) {
    trainer.setFirstName(trainerRegistration.firstName());
    trainer.setLastName(trainerRegistration.lastName());
    trainer.setUsername(username);
    trainer.setPassword(password);
    trainer.setActive(true);
    trainer.setSpecialization(trainingType);
  }
}

