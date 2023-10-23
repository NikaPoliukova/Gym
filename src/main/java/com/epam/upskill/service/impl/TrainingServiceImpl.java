package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.User;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;

  private final UserService userService;
  private final TraineeService traineeService;
  private final TrainerService trainerService;


  @Override
  public Training getTrainingById(long trainingId) {
    log.debug("Fetching Training by ID: " + trainingId);
    Training training = trainingRepository.findById(trainingId);
    if (training != null) {
      log.debug("Fetched Training details: " + training);
    } else {
      log.warn("Training not found for ID: " + trainingId);
    }
    return training;
  }

  @Override
  public void createTraining(TrainingDto trainingDto) {
    log.info("Creating Training: " + trainingDto);
    var trainee = traineeService.getTraineeById(trainingDto.traineeId());
    var trainer = trainerService.getTrainerById(trainingDto.trainerId());
    var trainingType = trainingRepository.findTrainingTypeById(trainingDto.trainingTypeId());
    var training = Training.builder().trainingName(trainingDto.trainingName())
        .trainingDate(trainingDto.trainingDate())
        .trainingDuration(trainingDto.trainingDuration()).trainingType(trainingType)
        .trainee(trainee)
        .trainer(trainer)
        .build();
    trainingRepository.save(training);
    log.debug("Training created: " + training);
  }

  @Override
  public List<Training> getTrainingsByUsernameAndCriteria(String username, String criteria) {
    User user = userService.findByUsername(username);
    if (user != null) {
      return trainingRepository.findTrainingsByUsernameAndCriteria(username, criteria);
    }
    return Collections.emptyList();
  }

  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    Trainee trainee = traineeService.getTraineeById(traineeId);
    if (trainee != null) {
      List<Trainer> activeTrainers = trainerService.findByIsActive();
      List<Trainer> notAssignedTrainers = new ArrayList<>();
      for (Trainer trainer : activeTrainers) {
        boolean isAssigned = false;
        for (Training training : trainer.getTrainings()) {
          if (training.getTrainee().equals(trainee)) {
            isAssigned = true;
            break;
          }
        }
        if (!isAssigned) {
          notAssignedTrainers.add(trainer);
        }
      }
      return notAssignedTrainers;
    }
    return Collections.emptyList();
  }
}
