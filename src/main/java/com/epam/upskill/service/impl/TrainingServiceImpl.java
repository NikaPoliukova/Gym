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

  //работает
  @Override
  @Transactional(readOnly = true)
  public Optional<Training> getTrainingById(long trainingId) {
    log.debug("Fetching Training by ID: " + trainingId);
    Optional<Training> training = trainingRepository.findById(trainingId);
    training.ifPresent(t -> log.debug("Fetched Training details: " + t));
    return training;
  }

  @Override//работает
  @Transactional(propagation = Propagation.REQUIRED)
  public void createTraining(TrainingDto trainingDto) {
    log.info("Creating Training: " + trainingDto);
    var trainee = traineeService.findById(trainingDto.traineeId());
    var trainer = trainerService.findById(trainingDto.trainerId());
    var trainingType = trainingRepository.findTrainingTypeById(trainingDto.trainingTypeId());
    var training = Training.builder()
        .trainingName(trainingDto.trainingName())
        .trainingDate(trainingDto.trainingDate())
        .trainingDuration(trainingDto.trainingDuration())
        .trainingType(trainingType)
        .trainee(trainee.get())
        .trainer(trainer.get())
        .build();
    trainingRepository.save(training);
    log.debug("Training created: " + training);
  }

  @Override//работает
  @Transactional(readOnly = true)
  public List<Training> getTrainingsByUsernameAndCriteria(String username, String criteria) {
    log.debug("Fetching Trainings by username: " + username + " and criteria: " + criteria);
    var user = userService.findByUsername(username);
    if (user.isEmpty()) {
      return Collections.emptyList();
    } else {
      return trainingRepository.findTrainingsByUsernameAndCriteria(username, criteria);
    }
  }

  @Override//работает
  @Transactional
  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    if (traineeService.findById(traineeId).isEmpty()) {
      return Collections.emptyList();
    }
    return trainingRepository.getNotAssignedActiveTrainersToTrainee(traineeId);
  }
}
