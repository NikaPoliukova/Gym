package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

  private final TrainerRepository trainerRepository;
  private final UserService userService;
  private final UserRepository userRepository;

  @Override
  public Trainer getTrainerById(long trainerId) {
    log.info("Fetching Trainer by ID: " + trainerId);
    var trainer = trainerRepository.findById(trainerId);
    if (trainer != null) {
      log.debug("Fetched Trainer details: " + trainer);
    } else {
      log.warn("Trainer not found for ID: " + trainerId);
    }
    return trainer;
  }

  @Override
  public Trainer getTrainerByUsername(String username) {
    return trainerRepository.findByUsername(username);
  }

  @Override
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  public List<Trainer> findActiveTrainers() {
    return trainerRepository.findByIsActive(true);
  }

  @Override
  public void createTrainer(@Valid TrainerRegistration trainerDto) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    String username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(), userRepository.findAll());
    String password = UserUtils.generateRandomPassword();
    var trainer = Trainer.builder()
        .username(username)
        .password(password)
        .build();
    userService.save(new PrepareUserDto(trainerDto.firstName(), trainerDto.lastName(), username, password));
    trainerRepository.save((Trainer) trainer);
    log.debug("Trainer created: " + trainer);
    //добавить сохранение в таблицу user,если данные были сохранены в таблицу trainee

    //настройка транзакции
  }

  public void updateTrainer(TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = trainerRepository.findById(trainerDto.id());
    if (trainer != null) {
      if (trainerDto.password() != null) {
        trainer.setPassword(trainerDto.password());
      }
      if (trainerDto.specialization() != null) {
        trainer.setSpecialization(trainerDto.specialization());
      }
      trainerRepository.update(trainer);
      userService.updateUser(new UserDto(trainerDto.id(), trainerDto.password()));
      log.debug("Trainer updated: " + trainer);
    } else {
      log.warn("Trainer not found for update: ID " + trainerDto.id());
    }
  }

  @Override
  public void updateTrainerPassword(TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = getTrainerById(trainerDto.id());
    if (trainer != null) {
      if (trainerDto.password() != null) {
        trainer.setPassword(trainerDto.password());
      }
      trainerRepository.update(trainer);
      userService.updateUser(new UserDto(trainerDto.id(), trainerDto.password()));
      log.debug("Trainer updated: " + trainer);
    } else {
      log.warn("Trainer not found for update: ID " + trainerDto.id());
    }
  }

  public void toggleTrainerActivation(long trainerId) {
    var trainer = getTrainerById(trainerId);
    long userId = trainer.getUser().getId();
    userService.toggleProfileActivation(userId);
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    log.info("Deleting Trainer by ID: " + trainerId);
    try {
      trainerRepository.delete(trainerId);
      log.debug("Trainer deleted successfully.");
    } catch (Exception e) {
      log.error("Failed to delete Trainer by ID: " + trainerId, e);
    }
  }

  @Override
  public List<Trainer> findByIsActive() {
    List<Trainer> activeTrainers = trainerRepository.findByIsActive(true);
    if (activeTrainers.isEmpty()) {
      return Collections.emptyList();
    }
    return activeTrainers;
  }
 }

