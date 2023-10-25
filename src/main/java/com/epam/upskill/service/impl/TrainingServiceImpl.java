package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;

  private final UserService userService;
  private final TraineeService traineeService;
  private final TrainerService trainerService;


  @Override
  @Transactional(readOnly = true)
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
  @Transactional(propagation = Propagation.REQUIRED)
  public void createTraining(TrainingDto trainingDto) {
    log.info("Creating Training: " + trainingDto);
    var trainee = traineeService.findById(trainingDto.traineeId());
    var trainer = trainerService.findById(trainingDto.trainerId());
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
  @Transactional(readOnly = true)
  public List<Training> getTrainingsByUsernameAndCriteria(String username, String criteria) {
    return Optional.ofNullable(userService.findByUsername(username))
        .map(user -> trainingRepository.findTrainingsByUsernameAndCriteria(username, criteria))
        .orElse(Collections.emptyList());
  }

  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    var trainee = traineeService.findById(traineeId);
    if (trainee == null) {
      return Collections.emptyList();
    }
    List<Trainer> activeTrainers = trainerService.findByIsActive();
    return activeTrainers.stream()
        .filter(trainer -> trainer.getTrainings().stream()
            .noneMatch(training -> training.getTrainee().equals(trainee))).toList();
  }
}
