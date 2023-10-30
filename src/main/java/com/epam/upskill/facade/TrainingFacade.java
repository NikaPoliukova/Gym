package com.epam.upskill.facade;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrainingFacade {
  private final TrainingService trainingService;

  public Training findTrainingById(long trainingId) {
    return trainingService.findTrainingById(trainingId);
  }

  public void saveTraining(TrainingDto trainingDto) {
    trainingService.saveTraining(trainingDto);
  }

  public List<Training> getTrainingsByUsernameAndCriteria(String username, String criteria) {
    return trainingService.findTrainingsByUsernameAndCriteria(username, criteria);
  }

  public List<Trainer> getNotAssignedActiveTrainersToTrainee(long traineeId) {
    return trainingService.findNotAssignedActiveTrainersToTrainee(traineeId);
  }
}
