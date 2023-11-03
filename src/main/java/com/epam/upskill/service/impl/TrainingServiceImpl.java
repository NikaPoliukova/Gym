package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.TrainingNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;
    private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingConverter trainingConverter;

  @Override
  @Transactional(readOnly = true)
  public Training findTrainingById(long trainingId) {
    log.debug("Fetching Training by ID: " + trainingId);
    return trainingRepository.findById(trainingId).orElseThrow(()
        -> new TrainingNotFoundException("trainingId = " + trainingId));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Training saveTraining(@Valid TrainingRequest request) {
    log.info("Creating Training: " + request);
    var trainee = traineeService.findByUsername(request.traineeUsername());
    var trainer = trainerService.findByUsername(request.trainerUsername());
    var trainingType = trainingRepository.findTrainingTypeByName(request.trainingType());
    var training = trainingConverter.toTraining(request, new Training());
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setTrainingType(trainingType);
    return trainingRepository.save(training);
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String periodFrom,
                                                           String periodTo, String trainerName, String trainingType) {
    return trainingRepository.findTrainingsByUsernameAndCriteria(username, periodFrom, periodTo,
        trainerName, trainingType);
  }

  @Override
  @Transactional
  public List<Trainer> findNotAssignedActiveTrainersToTrainee(String username) {
    var traineeId = traineeService.findByUsername(username).getId();
    List<Trainer> activeTrainers = trainingRepository.getAssignedActiveTrainersToTrainee(traineeId);
    List<Trainer> trainersList = trainerService.findAll();
    return trainersList.stream()
        .filter(trainer -> !activeTrainers.contains(trainer))
        .toList();

  }

  @Override
  public List<TrainingType> findTrainingTypes() {
    return trainingRepository.findTrainingTypes();
  }
}
