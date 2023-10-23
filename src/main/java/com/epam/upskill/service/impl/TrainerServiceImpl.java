package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.User;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
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
  @Transactional(readOnly = true)
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
  @Transactional(readOnly = true)
  public Trainer getTrainerByUsername(String username) {
    return (Trainer) userService.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Trainer> findAll() {
    log.debug("Fetching all Trainers");
    List<Trainer> trainerMap = trainerRepository.findAll();
    return trainerMap != null ? trainerMap : Collections.emptyList();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void createTrainer(@Valid TrainerRegistration trainerDto) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    var username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(), userRepository.findAll());
    var password = UserUtils.generateRandomPassword();
    userService.save(new PrepareUserDto(trainerDto.firstName(), trainerDto.lastName(), username, password));
    User user = userService.findByUsername(username);
    List<Training> trainings = new ArrayList<>();
    trainerRepository.save(new Trainer(trainerDto.specialization(), user, trainings));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateTrainer(@Valid TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    var trainer = trainerRepository.findById(trainerDto.id());
    trainer.setSpecialization(trainerDto.specialization());
    trainerRepository.update(trainer);
    userService.updateUser(new UserDto(trainerDto.id(), trainerDto.password()));
    log.debug("Trainer updated: " + trainer);
  }

  @Override
  @Transactional
  public void updateTrainerPassword(@Valid TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    userService.updatePassword(new UserDto(trainerDto.id(), trainerDto.password()));
  }


  @Override
  @Transactional(propagation = Propagation.REQUIRED)
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
    return trainerRepository.findByIsActive();
  }
}

