package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

  private final TrainerRepository trainerRepository;
  private final TraineeRepository traineeRepository;

  @Override
  public Trainer getTrainerById(long trainerId) {
    log.info("Fetching Trainer by ID: " + trainerId);
    Trainer trainer = trainerRepository.findById(trainerId);
    if (trainer != null) {
      log.debug("Fetched Trainer details: " + trainer);
    } else {
      log.warn("Trainer not found for ID: " + trainerId);
    }
    return trainer;
  }

  @Override
  public void createTrainer(TrainerRegistration trainerDto) {
    log.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    Trainer trainer = new Trainer();
    String username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(),
        traineeRepository.findAll(), trainerRepository.findAll());
    trainer.setUsername(username);
    trainer.setPassword(UserUtils.generateRandomPassword());
    trainerRepository.create(trainer);
    log.debug("Trainer created: " + trainer);
  }

  @Override
  public void updateTrainer(TrainerDto trainerDto) {
    log.info("Updating Trainer with TrainerDto: " + trainerDto);
    Trainer trainer = trainerRepository.findById(trainerDto.id());
    if (trainer != null) {
      if (trainerDto.password() != null) {
        trainer.setPassword(trainerDto.password());
      }
      if (trainerDto.specialization() != null) {
        trainer.setSpecialization(trainerDto.specialization());
      }
      trainerRepository.updateTrainer(trainer);
      log.debug("Trainer updated: " + trainer);
    } else {
      log.warn("Trainer not found for update: ID " + trainerDto.id());
    }
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    log.info("Deleting Trainer by ID: " + trainerId);
    try {
      trainerRepository.deleteTrainerById(trainerId);
      log.debug("Trainer deleted successfully.");
    } catch (Exception e) {
      log.error("Failed to delete Trainer by ID: " + trainerId, e);
    }
  }
}

