package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrainingServiceImpl implements TrainingService {
  private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
  private final TrainingRepository trainingRepository;

  @Override
  public Training getTrainingById(long traineeId) {
    return trainingRepository.findById(traineeId);
  }

  @Override
  public void createTraining(TrainingDto trainingDto) {
    Training training = new Training();
    training.setTrainingName(trainingDto.trainingName());
    training.setTrainingDate(trainingDto.trainingDate());
    training.setTrainingDuration(trainingDto.trainingDuration());
    training.setTrainingTypeId(trainingDto.trainingTypeId());
    trainingRepository.create(training);
  }
}
