package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
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
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Fetching Training by ID: {}", transactionId, trainingId);
    try {
      return trainingRepository.findById(trainingId).orElseThrow(()
          -> new TrainingNotFoundException("trainingId = " + trainingId));
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Training saveTraining(@Valid TrainingRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Creating Training: {}", transactionId, request);
    try {
      var trainee = traineeService.findByUsername(request.traineeUsername());
      var trainer = trainerService.findByUsername(request.trainerUsername());
      var trainingType = trainingRepository.findTrainingTypeByName(request.trainingType());
      var training = trainingConverter.toTraining(request, new Training());
      training.setTrainee(trainee);
      training.setTrainer(trainer);
      training.setTrainingType(trainingType);
      return trainingRepository.save(training);
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(String username, String periodFrom,
                                                           String periodTo, String trainerName, String trainingType) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    try {
      TrainingTypeEnum myEnum = TrainingTypeEnum.valueOf(trainingType);
      return trainingRepository.findTrainingsByUsernameAndCriteria(username, periodFrom, periodTo,
          trainerName, myEnum);
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    try {
      List<Training> trainings = trainingRepository.findTrainingsByUsernameAndCriteria(traineeId, trainingDate, trainingName);
      if (trainings.isEmpty()) {
        return Collections.emptyList();
      }
      return trainings;
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public List<Training> findTrainerTrainings(String username, String periodFrom, String periodTo,
                                             String traineeName) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    try {
      var trainerId = trainerService.findByUsername(username).getId();
      List<Training> trainings = trainingRepository.findTrainerTrainings(trainerId, periodFrom, periodTo, traineeName);
      if (trainings.isEmpty()) {
        return Collections.emptyList();
      }
      return trainings;
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional
  public List<Trainer> findNotAssignedActiveTrainersToTrainee(String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    try {
      var traineeId = traineeService.findByUsername(username).getId();
      List<Trainer> activeTrainers = trainingRepository.getAssignedActiveTrainersToTrainee(traineeId);
      List<Trainer> trainersList = trainerService.findAll();
      return trainersList.stream()
          .filter(trainer -> !activeTrainers.contains(trainer))
          .toList();
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public List<TrainingType> findTrainingTypes() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    try {
      return trainingRepository.findTrainingTypes();
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  public void delete(@NotNull Training training) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Deleting Training: {}", transactionId, training);
    try {
      trainingRepository.delete(training);
    } finally {
      MDC.remove("transactionId");
    }
  }

  @Override
  @Transactional
  public List<TrainerDtoForTrainee> updateTraineeTrainerList(String username, String trainingDate, String trainingName,
                                                             List<TrainersDtoList> list) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put("transactionId", transactionId);

    log.info("Transaction ID: {} | Updating Trainee Trainer List for Trainee with username: {}",
        transactionId, username);
    try {
      Trainee trainee = traineeService.findByUsername(username);
      List<Training> trainings = findTrainingsByUsernameAndCriteria(trainee.getId(), trainingDate, trainingName);
      Training patternTraining = trainings.get(0);
      trainings.forEach(trainingRepository::delete);

      List<Trainer> newTrainerList = new ArrayList<>();
      List<TrainerDtoForTrainee> newTrainerListForResponse = new ArrayList<>();
      for (TrainersDtoList trainerDto : list) {
        newTrainerList.add(trainerService.findByUsername(trainerDto.username()));
      }
      for (Trainer newTrainer : newTrainerList) {
        TrainingRequest trainingRequest = getTrainingRequest(trainee, patternTraining, newTrainer);
        Training training = saveTraining(trainingRequest);
        newTrainerListForResponse.add(getTrainerResponse(training));
      }
      return newTrainerListForResponse;
    } finally {
      MDC.remove("transactionId");
    }
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
