package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {
  private static final Logger logger = LoggerFactory.getLogger(TrainingServiceImpl.class);
  private final TrainingRepository trainingRepository;

  @Override
  public Training getTrainingById(long trainingId) {
    logger.debug("Fetching Training by ID: " + trainingId);
    Training training = trainingRepository.findById(trainingId);
    if (training != null) {
      logger.debug("Fetched Training details: " + training);
    } else {
      logger.warn("Training not found for ID: " + trainingId);
    }
    return training;
  }

  @Override
  public void createTraining(TrainingDto trainingDto) {
    logger.info("Creating Training: " + trainingDto);
    Training training = new Training();
    training.setTrainingName(trainingDto.trainingName());
    training.setTrainingDate(trainingDto.trainingDate());
    training.setTrainingDuration(trainingDto.trainingDuration());
    training.setTrainingTypeId(trainingDto.trainingTypeId());
    trainingRepository.create(training);
    logger.debug("Training created: " + training);
  }
}
