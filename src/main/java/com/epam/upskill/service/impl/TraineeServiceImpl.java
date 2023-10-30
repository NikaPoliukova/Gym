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

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

  private final TraineeRepository traineeRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;


  @Override
  @Transactional(readOnly = true)
  public Trainee findById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    return traineeRepository.findById(traineeId).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with id: " + traineeId));
  }

  @Override
  @Transactional(readOnly = true)
  public Trainee findByUsername(String username) {
    log.info("Fetching with username: {}", username);
    return traineeRepository.findByUsername(username).orElseThrow(()
        -> new TraineeNotFoundException("Trainee not found with username: " + username));
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
  public Trainee updateTrainee(@Valid TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    var trainee = findById(traineeDto.id());
    if (traineeDto.address() == null || traineeDto.address().isEmpty()) {
      throw new IllegalArgumentException("Address cannot be null or empty");
    }
    trainee.setAddress(traineeDto.address());
    return traineeRepository.update(trainee).get();
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long traineeId) {
    var trainee = findById(traineeId);
    var currentStatus = trainee.isActive();
    trainee.setActive(!currentStatus);
    traineeRepository.toggleProfileActivation(trainee);
  }
}


