package com.epam.upskill.service.impl;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeUpdateRequest;
import com.epam.upskill.dto.TrainerDtoForTrainee;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.logger.OperationLogger;
import com.epam.upskill.logger.TransactionLogger;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserService;
import com.epam.upskill.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {

  public static final String TRANSACTION_ID = "transactionId";

  private final TraineeRepository traineeRepository;
  private final UserService userService;
  private final TraineeConverter traineeConverter;
  private final TrainerConverter trainerConverter;
  private final TransactionLogger transactionLogger;
  private final OperationLogger operationLogger;

  @Override
  public Trainee findById(@NotNull long traineeId) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    transactionLogger.logTransaction(transactionId, "Performing transaction..." + transactionId);
    // Perform the transaction
    operationLogger.logOperation(transactionId, "findById", "operation findById цфы started");
    //log.debug("Transaction ID: {} | Fetching Trainee by ID: {}", transactionId, traineeId);
    try {
      return traineeRepository.findById(traineeId).orElseThrow(()
          -> new TraineeNotFoundException("Trainee not found with id: " + traineeId));
    } finally {
      operationLogger.logOperation(transactionId, "findById", "the operation was completed");
     //MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  public Trainee findByUsername(@NotBlank String username) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);

    log.info("Transaction ID: {} | Fetching with username: {}", transactionId, username);
    try {
      return traineeRepository.findByUsername(username).orElseThrow(()
          -> new TraineeNotFoundException("Trainee not found with username: " + username));
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  public Trainee findByUsernameAndPassword(String username, String password) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Fetching with username: {} and password = {}", transactionId, username, password);
    try {
      return traineeRepository.findByUsernameAndPassword(username, password).orElseThrow(()
          -> new TraineeNotFoundException("Trainee not found with username: " + username + " and password = " + password));
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Trainee> findAll() {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.debug("Transaction ID: {} | Fetching all Trainees", transactionId);
    try {
      List<Trainee> trainees = traineeRepository.findAll();
      return trainees != null ? trainees : Collections.emptyList();
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  @Transactional
  public Trainee saveTrainee(@Valid TraineeRegistration traineeDto) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Creating Trainee from TraineeRegistration: {}", transactionId, traineeDto);
    try {
      var username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(),
          userService.findAll());
      var password = UserUtils.generateRandomPassword();
      var trainee = traineeConverter.toTrainee(traineeDto);
      fillInTheTrainee(traineeDto, username, password, trainee);
      return traineeRepository.save(trainee);
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  @Transactional
  public Trainee updateTrainee(@Valid TraineeUpdateRequest request) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Updating Trainee with TraineeDto: {}", transactionId, request);
    try {
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
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  @Transactional
  public void toggleProfileActivation(long traineeId, boolean isActive) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.info("Transaction ID: {} | Toggling profile activation for Trainee with ID: {}", transactionId,
        traineeId);
    try {
      var trainee = findById(traineeId);
      if (trainee.isActive() != isActive) {
        trainee.setActive(isActive);
        traineeRepository.toggleProfileActivation(trainee);
      }
    } finally {
      MDC.remove(TRANSACTION_ID);
    }
  }

  @Override
  public List<TrainerDtoForTrainee> findTrainersForTrainee(long id) {
    String transactionId = UUID.randomUUID().toString();
    MDC.put(TRANSACTION_ID, transactionId);
    log.debug("Transaction ID: {} | Fetching trainers for Trainee with ID: {}", transactionId, id);
    try {
      var listTrainers = traineeRepository.findTrainersForTrainee(id);
      if (listTrainers.isEmpty()) {
        return Collections.emptyList();
      } else {
        return trainerConverter.toTrainerDtoForTrainee(listTrainers);
      }
    } finally {
      MDC.remove(TRANSACTION_ID);
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
}


