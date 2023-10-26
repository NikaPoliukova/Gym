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

  @Override //работает
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

  @Override//работает
  @Transactional(readOnly = true)
  public Trainer findByUsername(String username) {
    var trainer = trainerRepository.findByUsername(username);
    if (trainer == null) {
      log.error("Trainer not found with username: {}", username);
      throw new TrainerNotFoundException("Trainer not found with username: " + username);
    }
    log.info("Trainer found with username: {}", username);
    return trainer;
  }

  @Override//работает
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override//работает
  @Transactional
  public void createTrainer(@Valid TrainerRegistration trainerDto) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    var username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(), userService.findAll());
    var password = UserUtils.generateRandomPassword();
    Trainer trainer = trainerConverter.toTrainer(trainerDto);
    trainer.setUsername(username);
    trainer.setPassword(password);
    trainer.setActive(true);
    trainerRepository.save(trainer);
  }

  @Override//работает
  @Transactional
  public Trainer updateTrainer(@Valid TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = trainerRepository.findById(trainerDto.id());
    trainer.get().setSpecialization(trainerDto.specialization());
    return trainerRepository.update(trainer.get());
  }

  @Override//работает
  @Transactional
  public Trainer updateTrainerPassword(@Valid TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = findById(trainerDto.id());
    if (trainerDto.password() != null && !trainerDto.password().isEmpty()) {
      trainer.get().setPassword(trainerDto.password());
    }
    return trainerRepository.update(trainer.get());
  }

  @Override//работает
  @Transactional
  public void deleteTrainerById(long trainerId) {
    log.info("Deleting Trainer by ID: " + trainerId);
    try {
      trainerRepository.delete(trainerId);
      log.debug("Trainer deleted successfully.");
    } catch (Exception e) {
      log.error("Failed to delete Trainer by ID: " + trainerId, e);
    }
  }

  @Override//работает
  public List<Trainer> findByIsActive() {
    return trainerRepository.findByIsActive();
  }

  @Override//работает
  @Transactional
  public void toggleProfileActivation(long trainerId) {
    var trainer = findById(trainerId);
    var currentStatus = trainer.get().isActive();
    trainer.get().setActive(!currentStatus);
    trainerRepository.toggleProfileActivation(trainer.get());
  }
}

