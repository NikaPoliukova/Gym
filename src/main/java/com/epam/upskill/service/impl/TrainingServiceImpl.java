package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
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

import javax.validation.Valid;
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
  private final TrainingConverter trainingConverter;

  @Override
  @Transactional(readOnly = true)
  public Optional<Training> findTrainingById(long trainingId) {
    log.debug("Fetching Training by ID: " + trainingId);
    return Optional.ofNullable(trainingRepository.findById(trainingId).orElse(null));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Training saveTraining(@Valid TrainingDto trainingDto) {
    log.info("Creating Training: " + trainingDto);
    var trainee = traineeService.findById(trainingDto.traineeId());
    var trainer = trainerService.findById(trainingDto.trainerId());
    var trainingType = trainingRepository.findTrainingTypeById(trainingDto.trainingTypeId());
    var training = trainingConverter.toTraining(trainingDto, new Training());
    training.setTrainee(trainee.get());
    training.setTrainer(trainer.get());
    training.setTrainingType(trainingType);
    return trainingRepository.save(training);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String criteria) {
    log.debug("Fetching Trainings by username: " + username + " and criteria: " + criteria);
    var user = userService.findByUsername(username);
    if (user.isEmpty()) {
      return Collections.emptyList();
    } else {
      return trainingRepository.findTrainingsByUsernameAndCriteria(username, criteria);
    }
  }

  @Override
  @Transactional
  public List<Trainer> findNotAssignedActiveTrainersToTrainee(long traineeId) {
    if (traineeService.findById(traineeId).isEmpty()) {
      return Collections.emptyList();
    }
    return trainingRepository.getNotAssignedActiveTrainersToTrainee(traineeId);
  }
}
