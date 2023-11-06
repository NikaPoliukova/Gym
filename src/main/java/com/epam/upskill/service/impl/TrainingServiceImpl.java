package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.dto.TrainersDtoList;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.TrainingNotFoundException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.upskill.dto.TrainingTypeEnum.YOGA;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;
  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingConverter trainingConverter;
  private final TrainingTypeConverter trainingTypeConverter;

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
    TrainingTypeEnum myEnum = TrainingTypeEnum.valueOf(trainingType);
    return trainingRepository.findTrainingsByUsernameAndCriteria(username, periodFrom, periodTo,
        trainerName, myEnum);
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName) {
    List<Training> trainings = trainingRepository.findTrainingsByUsernameAndCriteria(traineeId, trainingDate, trainingName);
    if (trainings.isEmpty()) {
      return Collections.emptyList();
    }
    return trainings;
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

  @Override
  public void delete(Training training) {
    trainingRepository.delete(training);
  }

  @Override
  @Transactional
  public List<TrainerDtoForTrainee> updateTraineeTrainerList(String username, String trainingDate, String trainingName,
                                                             List<TrainersDtoList> list) {
    Trainee trainee = traineeService.findByUsername(username);
    List<Training> trainings = findTrainingsByUsernameAndCriteria(trainee.getId(), trainingDate, trainingName);
    Training patternTraining = trainings.get(0);
    trainings.forEach(trainingRepository::delete);

    List<Trainer> newTrainerList = new ArrayList<>();
    List<TrainerDtoForTrainee> newTrainerListForResponse = new ArrayList<>();
    for (TrainersDtoList trainerDto : list) {
      Trainer trainer = trainerService.findByUsername(trainerDto.username());
      newTrainerList.add(trainer);
    }

      for (Trainer newTrainer : newTrainerList) {
        TrainingRequest trainingRequest = new TrainingRequest(
            trainee.getUsername(),
            newTrainer.getUsername(),
            patternTraining.getTrainingName(),
            patternTraining.getTrainingDate(),
            patternTraining.getTrainingType().getTrainingTypeName().toString(),
            patternTraining.getTrainingDuration()
        );
        Training training = saveTraining(trainingRequest);
        TrainerDtoForTrainee trainerResponse = new TrainerDtoForTrainee(
            training.getTrainer().getUsername(),
            training.getTrainer().getFirstName(),
            training.getTrainer().getLastName(),
            training.getTrainer().getSpecialization().getTrainingTypeName().toString()
        );
        newTrainerListForResponse.add(trainerResponse);
      }

    return newTrainerListForResponse;
  }
}
