package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.impl.UserRepositoryImpl;
import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
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

  public Trainee getTraineeById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    return traineeRepository.findById(traineeId);
  }

  @Override
  public Trainee getTraineeByUsername(String username) {
    return traineeRepository.findByUsername(username);
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
  public void createTrainee(@Valid TraineeRegistration traineeDto) {
    log.info("Creating Trainee from TraineeRegistration: " + traineeDto);
    var username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(), userRepository.findAll());
    var password = UserUtils.generateRandomPassword();
    var trainee = Trainee.builder()
        .username(username)
        .password(password)
        .firstName(traineeDto.firstName())
        .lastName(traineeDto.lastName())//НЕ МОГУ ДОБАВИТЬ ПОЛЯ TRAINEE
        .build();
    userService.save(new PrepareUserDto(traineeDto.firstName(), traineeDto.lastName(), username, password));
    traineeRepository.save((Trainee) trainee);
    //добавить сохранение в таблицу user,если данные были сохранены в таблицу trainee
    //настройка транзакции
  }

  public void toggleTraineeActivation(long traineeId) {
    Trainee trainee = getTraineeById(traineeId);
    long userId = trainee.getUser().getId();
    userService.toggleProfileActivation(userId);
  }

  @Override
  public void updateTrainee(TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    var trainee = getTraineeById(traineeDto.id());
    if (traineeDto.password() != null) {
      trainee.setPassword(traineeDto.password());
    }
    if (traineeDto.address() != null) {
      trainee.setAddress(traineeDto.address());
    }
    userService.updateUser(new UserDto(traineeDto.id(), traineeDto.password()));
    traineeRepository.update(trainee);
  }

  @Override
  public void updateTraineePassword(TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    var trainee = getTraineeById(traineeDto.id());
    if (traineeDto.password() != null) {
      trainee.setPassword(traineeDto.password());
    }
    traineeRepository.update(trainee);
    userService.updateUser(new UserDto(traineeDto.id(), traineeDto.password()));
  }

  @Override
  public void deleteTraineeById(long traineeId) {
    log.info("Deleting Trainee by ID: " + traineeId);
    traineeRepository.delete(traineeId);
    deleteTraineeWithTrainings(traineeId);
  }

  public void deleteTraineeWithTrainings(long traineeId) {
    Trainee trainee = traineeRepository.findById(traineeId);
    if (trainee != null) {
      traineeRepository.delete(trainee.getId());
    }
  }
}

