package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {
  private final TrainerRepository trainerRepository;
  private final TraineeRepository traineeRepository;

  @Override
  public Trainer getTrainerById(long trainerId) {
    return trainerRepository.findById(trainerId);
  }

  @Override
  public void createTrainer(TrainerRegistration trainerDto) {
    Trainer trainer = new Trainer();
    String username = UserUtils.createUsername(trainerDto.firstName(), trainerDto.lastName(),
        traineeRepository.findAll(), trainerRepository.findAll());
    trainer.setUsername(username);
    trainer.setPassword(UserUtils.generateRandomPassword());
    trainerRepository.create(trainer);
  }

  @Override
  public void updateTrainer(TrainerDto trainerDto) {
    Trainer trainer = trainerRepository.findById(trainerDto.id());
    if (trainerDto.password() != null) {
      trainer.setPassword(trainerDto.password());
    }
    if (trainerDto.specialization() != null) {
      trainer.setSpecialization(trainerDto.specialization());
    }
    trainerRepository.updateTrainer(trainer);
  }

  @Override
  public void deleteTrainerById(long trainerId) {
    trainerRepository.deleteTrainerById(trainerId);
  }
}
