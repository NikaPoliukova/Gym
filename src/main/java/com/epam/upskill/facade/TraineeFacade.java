package com.epam.upskill.facade;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TraineeFacade {
  private final TraineeService traineeService;

  @Autowired
  public TraineeFacade(TraineeService traineeService) {
    this.traineeService = traineeService;
  }

  public Trainee getTraineeById(long traineeId) {
    return traineeService.getTraineeById(traineeId);
  }

  public Map<Long, Trainee> findAll() {
    return traineeService.findAll();
  }


  public void createTrainee(TraineeRegistration traineeDto) {
    traineeService.createTrainee(traineeDto);
  }


  public void updateTrainee(TraineeDto traineeDto) {
    traineeService.updateTrainee(traineeDto);
  }


  public void deleteTraineeById(long traineeId) {
    traineeService.deleteTraineeById(traineeId);
  }
}
