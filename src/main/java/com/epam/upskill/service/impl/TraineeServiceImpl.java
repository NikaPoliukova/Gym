package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.exception.JsonMappingException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  @Autowired
  public TraineeServiceImpl(TraineeRepository traineeRepository, TrainerRepository trainerRepository) {
    this.traineeRepository = traineeRepository;
    this.trainerRepository = trainerRepository;
  }

  @Override

  public Trainee getTraineeById(long traineeId) {
    log.debug("Fetching Trainee by ID: " + traineeId);
    return traineeRepository.findById(traineeId);
  }

  @Override
  @Transactional(readOnly = true)
  public Map<Long, Trainee> findAll() {
    log.debug("Fetching all Trainees");
    Map<Long, Trainee> trainees = traineeRepository.findAll();
    return trainees != null ? trainees : Collections.emptyMap();
  }

  @Override
  @Transactional(rollbackFor = JsonMappingException.class)
    public void createTrainee(TraineeRegistration traineeDto) {
    log.info("Creating Trainee from TraineeRegistration: " + traineeDto);
    Trainee trainee = new Trainee();
    String username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(),
        traineeRepository.findAll(), trainerRepository.findAll());
    trainee.setUsername(username);
    trainee.setPassword(UserUtils.generateRandomPassword());
    trainee.setFirstName(traineeDto.firstName());
    trainee.setLastName(traineeDto.lastName());
    trainee.setAddress(traineeDto.address());
    trainee.setDateOfBirth(traineeDto.dateOfBirth());
    traineeRepository.create(trainee);
  }

  @Override

  public void updateTrainee(TraineeDto traineeDto) {
    log.info("Updating Trainee with TraineeDto: " + traineeDto);
    Trainee trainee = traineeRepository.findById(traineeDto.id());
    if (traineeDto.password() != null) {
      trainee.setPassword(traineeDto.password());
    }
    if (traineeDto.address() != null) {
      trainee.setAddress(traineeDto.address());
    }
    traineeRepository.updateTrainee(trainee);
  }

  @Override
  public void deleteTraineeById(long traineeId) {
    log.info("Deleting Trainee by ID: " + traineeId);
    traineeRepository.deleteTraineeById(traineeId);
  }
}

