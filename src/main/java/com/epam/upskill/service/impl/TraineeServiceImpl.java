package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.exception.UserNotFoundException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public Trainee findById(long traineeId) {
    return traineeRepository.findById(traineeId).orElseThrow(()
        -> new UserNotFoundException("trainee with id = " + traineeId));
  }

  @Override
  @Transactional
  public Trainee findByUsername(String username) {
    return traineeRepository.findByUsername(username).orElseThrow(()
        -> new UserNotFoundException(username));

  }

  @Override
  @Transactional
  public Trainee findByUsernameAndPassword(String username, String password) {
    return traineeRepository.findByUsernameAndPassword(username, password).orElseThrow(()
        -> new UserNotFoundException(username));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Trainee> findAll() {
    List<Trainee> trainees = traineeRepository.findAll();
    return trainees != null ? trainees : Collections.emptyList();
  }

  @Override
  @Transactional
  public Trainee saveTrainee(TraineeRegistration traineeDto) {
    var username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(),
        userService.findAll());
    var password = UserUtils.generateRandomPassword();
    var trainee = traineeConverter.toTrainee(traineeDto);
    fillInTheTrainee(traineeDto, username, password, trainee);
    return traineeRepository.save(trainee);
  }

  @Override
  @Transactional
  public Trainee updateTrainee(TraineeUpdateRequest request) {
    var trainee = findByUsername(request.username());
    fillTrainee(request, trainee);
    if (request.isActive() != trainee.isActive()) {
      trainee.setActive(request.isActive());
    }
    return traineeRepository.update(trainee).get();

  }

  @Override
  @Transactional
  public void toggleProfileActivation(long traineeId, boolean isActive) {
    var trainee = findById(traineeId);
    if (trainee.isActive() != isActive) {
      trainee.setActive(isActive);
      traineeRepository.toggleProfileActivation(trainee);
    }
  }


  @Override
  @Transactional
  public List<TrainerDtoForTrainee> findTrainersForTrainee(long id) {
    var listTrainers = traineeRepository.findTrainersForTrainee(id);
    if (listTrainers.isEmpty()) {
      return Collections.emptyList();
    } else {
      return trainerConverter.toTrainerDtoForTrainee(listTrainers);
    }
  }

  private static void fillInTheTrainee(TraineeRegistration traineeDto, String username, String password,
                                       Trainee trainee) {
    trainee.setFirstName(traineeDto.firstName());
    trainee.setLastName(traineeDto.lastName());
    trainee.setPassword(password);
    trainee.setUsername(username);
    trainee.setActive(true);
  }

  private static void fillTrainee(TraineeUpdateRequest request, Trainee trainee) {
    Optional.ofNullable(request.firstName()).filter(firstName -> !firstName.isEmpty())
        .ifPresent(trainee::setFirstName);
    Optional.ofNullable(request.lastName()).filter(lastName -> !lastName.isEmpty())
        .ifPresent(trainee::setLastName);
    Optional.ofNullable(request.dateOfBirth()).ifPresent(trainee::setDateOfBirth);
    Optional.ofNullable(request.address()).filter(address -> !address.isEmpty())
        .ifPresent(trainee::setAddress);
  }

}


