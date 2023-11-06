package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

  private final TraineeRepository traineeRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;
  private final TrainerConverter trainerConverter;


  @Override
  @Transactional(readOnly = true)
  public Trainee findById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    return traineeRepository.findById(traineeId).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with id: " + traineeId));
  }

  @Override
  public Trainee findByUsername(String username) {
    log.info("Fetching with username: {}", username);
    return traineeRepository.findByUsername(username).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with username: " + username));
  }

  @Override
  public Trainee findByUsernameAndPassword(String username, String password) {
    log.info("Fetching with username: = " + username + " and password = " + password);
    return traineeRepository.findByUsernameAndPassword(username, password).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with username: = " + username + " and password = " + password));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Trainee> findAll() {
    log.debug("Fetching all Trainees");
    List<Trainee> trainees = traineeRepository.findAll();
    return trainees != null ? trainees : Collections.emptyList();
  }

  @Override
  @Transactional
  public Trainee saveTrainee(@Valid TraineeRegistration traineeDto) {
    log.info("Creating Trainee from TraineeRegistration: " + traineeDto);
    var username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(), userService.findAll());
    var password = UserUtils.generateRandomPassword();
    var trainee = traineeConverter.toTrainee(traineeDto);
    trainee.setFirstName(traineeDto.firstName());
    trainee.setLastName(traineeDto.lastName());
    trainee.setPassword(password);
    trainee.setUsername(username);
    trainee.setActive(true);
    return traineeRepository.save(trainee);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Trainee updateTrainee(TraineeUpdateRequest request) {
    log.info("Updating Trainee with TraineeDto: " + request);
    var trainee = findByUsername(request.username());
    Optional.ofNullable(request.firstName()).filter(firstName -> !firstName.isEmpty())
        .ifPresent(trainee::setFirstName);
    Optional.ofNullable(request.lastName()).filter(lastName -> !lastName.isEmpty())
        .ifPresent(trainee::setLastName);
    Optional.ofNullable(request.dateOfBirth()).ifPresent(trainee::setDateOfBirth);
    Optional.ofNullable(request.address()).filter(address -> !address.isEmpty())
        .ifPresent(trainee::setAddress);
    if (request.isActive() != trainee.isActive()) {
      trainee.setActive(request.isActive());
    }
    return traineeRepository.update(trainee).get();
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long traineeId, boolean isActive) {
    var trainee = findById(traineeId);
    trainee.setActive(isActive);
    traineeRepository.toggleProfileActivation(trainee);
  }


  @Override
  public List<TrainerDtoForTrainee> findTrainersForTrainee(long id) {
    var listTrainers = traineeRepository.findTrainersForTrainee(id);
    if (listTrainers.isEmpty()) {
      return Collections.emptyList();
    } else {
      return trainerConverter.toTrainerDtoForTrainee(listTrainers);
    }
  }

//  @Override
//  public List<TrainerDtoForTrainee> updateTraineeTrainerList(String username, String trainingDate, String trainingName,
//                                                             List<TrainersDtoList> list) {
//    Trainee trainee = findByUsername(username);
//    List<Training> trainings = trainingRepository.findTrainingsByUsernameAndCriteria(trainee.getId(), trainingDate, trainingName);
//    Training patternTraining = trainings.get(0);
//    // Создаем новый список тренеров и список DTO для ответа
//    List<TrainerDtoForTrainee> newTrainerList = new ArrayList<>();
//    List<Trainer> newTrainers = list.stream()
//        .map(trainerDto -> trainerService.findByUsername(trainerDto.username()))
//        .toList();
//    // Удаляем старые тренировки
//    trainings.forEach(trainingRepository::delete);
//    // Создаем новые тренировки и записываем тренеров в список DTO
//    for (Trainer newTrainer : newTrainers) {
//      TrainingRequest trainingRequest = new TrainingRequest(
//          trainee.getUsername(),
//          newTrainer.getUsername(),
//          patternTraining.getTrainingName(),
//          patternTraining.getTrainingDate(),
//          patternTraining.getTrainingType().getTrainingTypeName().toString(),
//          patternTraining.getTrainingDuration()
//      );
//      Training training = trainingService.saveTraining(trainingRequest);
//      TrainerDtoForTrainee trainerDto = new TrainerDtoForTrainee(
//          training.getTrainer().getUsername(),
//          training.getTrainer().getFirstName(),
//          training.getTrainer().getLastName(),
//          training.getTrainer().getSpecialization().getTrainingTypeName().toString()
//      );
//      newTrainerList.add(trainerDto);
//    }
//    return newTrainerList;
//  }
}


