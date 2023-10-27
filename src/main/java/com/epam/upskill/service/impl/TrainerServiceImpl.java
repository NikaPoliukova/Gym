package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.exception.TrainerNotFoundException;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

  private final TrainerRepository trainerRepository;
  private final UserService userService;
  private final TrainerConverter trainerConverter;

  @Override
  @Transactional
  public Optional<Trainer> findById(long trainerId) {
    log.info("Fetching Trainer by ID: " + trainerId);
    var trainer = trainerRepository.findById(trainerId);
    if (trainer.isEmpty()) {
      log.warn("Trainer not found for ID: " + trainerId);
      throw new TraineeNotFoundException("Trainee not found with id: " + trainerId);
    }
    log.info("Trainee found with id: {}", trainerId);
    return trainer;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Trainer> findByUsername(String username) {
    var trainer = trainerRepository.findByUsername(username);
    if (trainer.isEmpty()) {
      log.error("Trainer not found with username: {}", username);
      throw new TrainerNotFoundException("Trainer not found with username: " + username);
    }
    log.info("Trainer found with username: {}", username);
    return trainer;
  }

  @Override
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override
  @Transactional
  public Trainer saveTrainer(@Valid TrainerRegistration trainerDto) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    var username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(), userService.findAll());
    var password = UserUtils.generateRandomPassword();
    Trainer trainer = trainerConverter.toTrainer(trainerDto);
    trainer.setFirstName(trainerDto.firstName());
    trainer.setLastName(trainerDto.lastName());
    trainer.setUsername(username);
    trainer.setPassword(password);
    trainer.setActive(true);
    return trainerRepository.save(trainer);
  }

  @Override
  @Transactional
  public Optional<Trainer> updateTrainer(@Valid TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = trainerRepository.findById(trainerDto.id());
    trainer.get().setSpecialization(trainerDto.specialization());
    return Optional.ofNullable(trainerRepository.update(trainer.get()));
  }

  @Override
  public List<Trainer> findByIsActive() {
    List<Trainer> activeTrainers = trainerRepository.findByIsActive();
    if (activeTrainers.isEmpty()) {
      return Collections.emptyList();
    }
    return activeTrainers;
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long trainerId) {
    var trainer = findById(trainerId);
    var currentStatus = trainer.get().isActive();
    trainer.get().setActive(!currentStatus);
    trainerRepository.toggleProfileActivation(trainer.get());
  }
}

