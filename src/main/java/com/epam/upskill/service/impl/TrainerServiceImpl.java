package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TraineeDtoForTrainer;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.dto.TrainerUpdateRequest;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.exception.TrainerNotFoundException;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

  private final TrainerRepository trainerRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;
  private final TrainerConverter trainerConverter;
  private final TrainingRepository trainingRepository;

  @Override
  @Transactional
  public Trainer findById(long trainerId) {
    log.info("Fetching Trainer by ID: " + trainerId);
    return trainerRepository.findById(trainerId).orElseThrow(()
        -> new TrainerNotFoundException("Trainer not found with id: " + trainerId));
  }

  @Override
  @Transactional(readOnly = true)
  public Trainer findByUsername(String username) {
    log.info("Fetching Trainer by username: " + username);
    return trainerRepository.findByUsername(username).orElseThrow(()
        -> new TrainerNotFoundException("Trainer not found with username: " + username));
  }

  @Override
  public Trainer findByUsernameAndPassword(String username, String password) {
    log.info("Fetching with username: = " + username + " and password = " + password);
    return trainerRepository.findByUsernameAndPassword(username, password).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with username: = " + username + " and password = " + password));
  }

  @Override
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override
  @Transactional
  public Trainer saveTrainer(TrainerRegistration trainerRegistration) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerRegistration);
    var username = UserUtils.createUsername(trainerRegistration.firstName(), trainerRegistration.lastName(),
        userService.findAll());
    var password = UserUtils.generateRandomPassword();
    Trainer trainer = new Trainer();
    TrainingType trainingType =trainingRepository.findTrainingTypeByName(trainerRegistration.specialization());
//    Trainer trainer = trainerConverter.toTrainer(trainerRegistration);
    trainer.setFirstName(trainerRegistration.firstName());
    trainer.setLastName(trainerRegistration.lastName());
    trainer.setUsername(username);
    trainer.setPassword(password);
    trainer.setActive(true);
    trainer.setSpecialization(trainingType);
    return trainerRepository.save(trainer);
  }

  @Override
  @Transactional
  public Trainer updateTrainer(TrainerUpdateRequest request) {
    log.info("Updating Trainer with TrainerDto: " + request);
    var trainer = trainerRepository.findByUsername(request.username()).get();
    trainerConverter.toTrainer(request);
    return trainerRepository.update(trainer);
  }

  @Override
  public List<Trainer> findByIsActive() {
    List<Trainer> activeTrainers = trainerRepository.findByIsActive();
    return activeTrainers.isEmpty() ? Collections.emptyList() : activeTrainers;
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long trainerId) {
    var trainer = findById(trainerId);
    var currentStatus = trainer.isActive();
    trainer.setActive(!currentStatus);
    trainerRepository.toggleProfileActivation(trainer);
  }

  @Override
  public List<TraineeDtoForTrainer> findTraineesForTrainer(long id) {
    List<Trainee> listTrainees = trainerRepository.findTraineesForTrainer(id);
    if (listTrainees.isEmpty()) {
      return Collections.emptyList();
    } else {
      return traineeConverter.toTraineeDtoForTrainer(listTrainees);
    }
  }
}

