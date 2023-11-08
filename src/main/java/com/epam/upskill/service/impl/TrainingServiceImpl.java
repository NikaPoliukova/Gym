package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.epam.upskill.service.impl.TraineeServiceImpl.TRANSACTION_ID;

@Slf4j
@RequiredArgsConstructor
@Service
@Validated
public class TrainingServiceImpl implements TrainingService {

  private final TrainingRepository trainingRepository;
  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingConverter trainingConverter;


  @Override
  @Transactional(readOnly = true)
  public Training findTrainingById(long trainingId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching Training by ID: {}", transactionId, trainingId);
    return trainingRepository.findById(trainingId).orElseThrow(()
        -> new OperationFailedException(" training id", "find training by id"));

  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Training saveTraining(@Valid TrainingRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Creating Training: {}", transactionId, request);

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
  public List<Training> findTrainingsByUsernameAndCriteria(@Valid TrainingTraineeRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    TrainingTypeEnum myEnum = TrainingTypeEnum.valueOf(request.trainingType());
    return trainingRepository.findTraineeTrainingsList(new TrainingDtoRequest(request.username(),
        request.periodFrom(), request.periodTo(), request.trainerName(), myEnum));
  }

  @Override
  @Transactional
  public List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    List<Training> trainings = trainingRepository.findTraineeTrainingsList(traineeId, trainingDate, trainingName);
    if (trainings.isEmpty()) {
      return Collections.emptyList();
    }
    return trainings;
  }

  @Override
  @Transactional
  public List<Training> findTrainerTrainings(@Valid TrainingTrainerRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);

    var trainerId = trainerService.findByUsername(request.username()).getId();
    List<Training> trainings = trainingRepository.findTrainerTrainings
        (new TrainingTrainerDto(trainerId, request.periodFrom(), request.periodTo(), request.traineeName()));
    if (trainings.isEmpty()) {
      return Collections.emptyList();
    }
    return trainings;
  }

  @Override
  @Transactional
  public List<Trainer> findNotAssignedActiveTrainersToTrainee(String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    var traineeId = traineeService.findByUsername(username).getId();
    List<Trainer> activeTrainers = trainingRepository.getAssignedActiveTrainersToTrainee(traineeId);
    List<Trainer> trainersList = trainerService.findAll();
    return trainersList.stream()
        .filter(trainer -> !activeTrainers.contains(trainer))
        .toList();
  }

  @Override
  @Transactional
  public List<TrainingType> findTrainingTypes() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    return trainingRepository.findTrainingTypes();
  }

  @Override
  @Transactional
  public void delete(@NotNull Training training) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Deleting Training: {}", transactionId, training);
    trainingRepository.delete(training);
  }

  @Override
  @Transactional
  public List<TrainerDtoForTrainee> updateTraineeTrainerList(@Valid UpdateTraineeTrainerDto dto) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);

    log.info("Transaction ID: {} | Updating Trainee Trainer List for Trainee with username: {}",
        transactionId, dto.username());

    Trainee trainee = traineeService.findByUsername(dto.username());
    List<Training> trainings = findTrainingsByUsernameAndCriteria(trainee.getId(), dto.trainingDate(), dto.trainingName());
    Training patternTraining = trainings.get(0);
    trainings.forEach(trainingRepository::delete);

    List<Trainer> newTrainerList = new ArrayList<>();
    List<TrainerDtoForTrainee> newTrainerListForResponse = new ArrayList<>();
    for (TrainersDtoList trainerDto : dto.list()) {
      newTrainerList.add(trainerService.findByUsername(trainerDto.username()));
    }
    for (Trainer newTrainer : newTrainerList) {
      TrainingRequest trainingRequest = getTrainingRequest(trainee, patternTraining, newTrainer);
      Training training = saveTraining(trainingRequest);
      newTrainerListForResponse.add(getTrainerResponse(training));
    }
    return newTrainerListForResponse;

  }


  private static TrainerDtoForTrainee getTrainerResponse(Training training) {
    return new TrainerDtoForTrainee(
        training.getTrainer().getUsername(),
        training.getTrainer().getFirstName(),
        training.getTrainer().getLastName(),
        training.getTrainer().getSpecialization().getTrainingTypeName().toString()
    );
  }

  private static TrainingRequest getTrainingRequest(Trainee trainee, Training patternTraining, Trainer newTrainer) {
    return new TrainingRequest(
        trainee.getUsername(),
        newTrainer.getUsername(),
        patternTraining.getTrainingName(),
        patternTraining.getTrainingDate(),
        patternTraining.getTrainingType().getTrainingTypeName().toString(),
        patternTraining.getTrainingDuration()
    );
  }
}
