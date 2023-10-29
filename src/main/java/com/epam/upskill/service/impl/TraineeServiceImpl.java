package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
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


  @Override
  @Transactional(readOnly = true)
  public Optional<Trainee> findById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    var trainee = traineeRepository.findById(traineeId);
    if (trainee.isEmpty()) {
      log.error("Trainee not found with id: {}", traineeId);
      throw new TraineeNotFoundException("Trainee not found with id: " + traineeId);
    }
    log.info("Trainee found with id: {}", traineeId);
    return trainee;
  }

  @Override
  @Transactional(readOnly = true)
  public Trainee findByUsername(String username) {
    var trainee = traineeRepository.findByUsername(username);
    if (trainee.isEmpty()) {
      log.error("Trainee not found with username: {}", username);
      throw new TraineeNotFoundException("Trainee not found with username: " + username);
    }
    log.info("Trainee found with username: {}", username);
    return trainee.get();
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
  public Optional<Trainee> updateTrainee(@Valid TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    var trainee = findById(traineeDto.id());
    if (traineeDto.address() == null || traineeDto.address().isEmpty()) {
      throw new IllegalArgumentException("Address cannot be null or empty");
    }
    trainee.ifPresent(t -> t.setAddress(traineeDto.address()));
    return traineeRepository.update(trainee.orElse(null));
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long traineeId) {
    var trainee = findById(traineeId);
    var currentStatus = trainee.get().isActive();
    trainee.get().setActive(!currentStatus);
    traineeRepository.toggleProfileActivation(trainee.get());
  }
}


