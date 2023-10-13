package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {
  private static final Logger logger = LoggerFactory.getLogger(TrainerServiceImpl.class);

  private final TrainerRepository trainerRepository;
  private final TraineeRepository traineeRepository;

  @Override
  public Trainer getTrainerById(long trainerId) {
    logger.info("Fetching Trainer by ID: " + trainerId);
    Trainer trainer = trainerRepository.findById(trainerId);
    if (trainer != null) {
      logger.debug("Fetched Trainer details: " + trainer);
    } else {
      logger.warn("Trainer not found for ID: " + trainerId);
    }
    return trainer;
  }

  @Override
  public void createTrainer(TrainerRegistration trainerDto) {
    logger.info("Creating Trainer from TrainerRegistration: " + trainerDto);
    Trainer trainer = new Trainer();
    String username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(),
        traineeRepository.findAll(), trainerRepository.findAll());
    trainer.setUsername(username);
    trainer.setPassword(UserUtils.generateRandomPassword());
    trainerRepository.create(trainer);
    logger.debug("Trainer created: " + trainer);
  }

  @Override
  public void updateTrainer(TrainerDto trainerDto) {
    logger.info("Updating Trainer with TrainerDto: " + trainerDto);
    Trainer trainer = trainerRepository.findById(trainerDto.id());
    if (trainer != null) {
      if (trainerDto.password() != null) {
        trainer.setPassword(trainerDto.password());
      }
      if (trainerDto.specialization() != null) {
        trainer.setSpecialization(trainerDto.specialization());
      }
      trainerRepository.updateTrainer(trainer);
      logger.debug("Trainer updated: " + trainer);
    } else {
      logger.warn("Trainer not found for update: ID " + trainerDto.id());
    }
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    logger.info("Deleting Trainer by ID: " + trainerId);
    try {
      trainerRepository.deleteTrainerById(trainerId);
      logger.debug("Trainer deleted successfully.");
    } catch (Exception e) {
      logger.error("Failed to delete Trainer by ID: " + trainerId, e);
    }
  }
}

