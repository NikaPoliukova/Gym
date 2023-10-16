package com.epam.upskill.facade;

import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TrainerFacade {

  private final TrainerService trainerService;

  @Autowired
  public TrainerFacade(TrainerService trainerService) {
    this.trainerService = trainerService;
  }

  public Trainer getTrainerById(long trainerId) {
    return trainerService.getTrainerById(trainerId);
  }

  public Map<Long, Trainer> findAll() {
    return trainerService.findAll();
  }

  public void createTrainer(TrainerRegistration trainerDto) {
    trainerService.createTrainer(trainerDto);
  }

  public void updateTrainer(TrainerDto trainerDto) {
    trainerService.updateTrainer(trainerDto);
  }

  public void deleteTrainerById(long trainerId) {
    trainerService.deleteTrainerById(trainerId);
  }
}
