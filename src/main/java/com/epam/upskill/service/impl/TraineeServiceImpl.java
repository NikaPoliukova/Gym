package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TraineeServiceImpl implements TraineeService {
  private final TraineeRepository traineeRepository;
  private final TrainerRepository trainerRepository;

  @Override
  public Trainee getTraineeById(long traineeId) {
    return traineeRepository.findById(traineeId);
  }

  @Override
  public void createTrainee(TraineeRegistration traineeDto) {
    Trainee trainee = new Trainee();
    String username = UserUtils.createUsername(traineeDto.firstName(), traineeDto.lastName(),
        traineeRepository.findAll(), trainerRepository.findAll());
    trainee.setUsername(username);
    trainee.setPassword(UserUtils.generateRandomPassword());
    trainee.setFirstName(traineeDto.firstName());
    trainee.setLastName(traineeDto.lastName());
    trainee.setAddress(traineeDto.address());
    trainee.setDate(traineeDto.dateOfBirth());
    traineeRepository.create(trainee);
  }

  @Override
  public void updateTrainee(TraineeDto traineeDto) {
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
    traineeRepository.deleteTraineeById(traineeId);
  }
}
