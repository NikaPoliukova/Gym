package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.impl.UserRepositoryImpl;
import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.User;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

  private final TraineeRepository traineeRepository;
  private final UserRepositoryImpl userRepository;
  private final UserService userService;

  @Override
  @Transactional(readOnly = true)
  public Trainee getTraineeById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    return traineeRepository.findById(traineeId);
  }

  @Override
  @Transactional(readOnly = true)
  public Trainee getTraineeByUsername(String username) {
    return (Trainee) userService.findByUsername(username);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Trainee> findAll() {
    log.debug("Fetching all Trainees");
    List<Trainee> trainees = traineeRepository.findAll();
    return trainees != null ? trainees : Collections.emptyList();
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void createTrainee(@Valid TraineeRegistration traineeDto) {
    log.info("Creating Trainee from TraineeRegistration: " + traineeDto);
    var username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(), userRepository.findAll());
    var password = UserUtils.generateRandomPassword();
    userService.save(new PrepareUserDto(traineeDto.firstName(), traineeDto.lastName(), username, password));
    User user = userService.findByUsername(username);
    List<Training> trainings = new ArrayList<>();
    traineeRepository.save(new Trainee(traineeDto.dateOfBirth(), traineeDto.address(), user, trainings));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateTrainee(@Valid TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    var trainee = getTraineeById(traineeDto.id());
    if (traineeDto.address() != null) {
      trainee.setAddress(traineeDto.address());
    }
    traineeRepository.update(trainee);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateTraineePassword(@Valid TraineeDto traineeDto) {
    log.info("Updating Trainee's password: ");
    userService.updatePassword(new UserDto(traineeDto.id(), traineeDto.password()));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void deleteTraineeById(long traineeId) {
    log.info("Deleting Trainee by ID: " + traineeId);
    traineeRepository.delete(traineeId);
  }
}

