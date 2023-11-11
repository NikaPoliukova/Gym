package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

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
    return trainingRepository.findById(trainingId).orElseThrow(()
        -> new OperationFailedException(" training id", "find training by id"));

  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Training saveTraining(TrainingRequest request) {
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
  public List<Training> findTrainingsByUsernameAndCriteria(TrainingTraineeRequest request) {
    if (request.periodFrom() != null && request.periodTo() != null && request.periodFrom().isAfter(request.periodTo())) {
      throw new IllegalArgumentException("periodFrom must be before or equal to periodTo");
    }
    if (request.trainingType() == null) {
      return trainingRepository.findTraineeTrainingsList(request.username(), request.periodFrom(), request.periodTo(),
          request.trainerName());
    }
    return trainingRepository.findTraineeTrainingsList(new TrainingDtoRequest(request.username(),
        request.periodFrom(), request.periodTo(), request.trainerName(), TrainingTypeEnum.valueOf(request.trainingType())));
  }

  @Override
  @Transactional
  public List<Training> findTrainingsByUsernameAndCriteria(long traineeId, String trainingDate, String trainingName) {
    List<Training> trainings = trainingRepository.findTraineeTrainingsList(traineeId, trainingDate, trainingName);
    if (trainings.isEmpty()) {
      return Collections.emptyList();
    }
    return trainings;
  }

  @Override
  @Transactional
  public List<Training> findTrainerTrainings(TrainingTrainerRequest request) {
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
    return trainingRepository.findTrainingTypes();
  }

  @Override
  @Transactional
  public void delete(Training training) {
    trainingRepository.delete(training);
  }

  @Override
  @Transactional
  public List<TrainerDtoForTrainee> updateTraineeTrainerList(UpdateTraineeTrainerDto dto) {
    Trainee trainee = traineeService.findByUsername(dto.username());
    checkForNewTrainerFor(dto);
    List<Training> trainings = findTrainingsByUsernameAndCriteria(trainee.getId(), dto.trainingDate(), dto.trainingName());
    Training patternTraining = trainings.get(0);
    trainings.forEach(trainingRepository::delete);
    return dto.list().stream()
        .map(trainerDto -> trainerService.findByUsername(trainerDto.username()))
        .map(newTrainer -> {
          TrainingRequest trainingRequest = getTrainingRequest(trainee, patternTraining, newTrainer);
          Training training = saveTraining(trainingRequest);
          return getTrainerResponse(training);
        })
        .toList();
  }

  private void checkForNewTrainerFor(UpdateTraineeTrainerDto dto) {
    List<TrainersDtoList> dtoList = dto.list();
    for (TrainersDtoList tdl : dtoList) {
      try {
        trainerService.findByUsername(tdl.username());
      } catch (UserNotFoundException ex) {
        throw new OperationFailedException("one of the trainer username was not found", "updateTraineeTrainerList");
      }
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
