package com.epam.upskill.service.impl;

import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingServiceImpl implements TrainingService {
  private final TrainingRepository trainingRepository;

  @Override
  public Training getTrainingById(long trainingId) {
    log.debug("Fetching Training by ID: " + trainingId);
    Training training = trainingRepository.findById(trainingId);
    if (training != null) {
      log.debug("Fetched Training details: " + training);
    } else {
      log.warn("Training not found for ID: " + trainingId);
    }
    return training;
  }

  @Override
  public void createTraining(TrainingDto trainingDto) {
    log.info("Creating Training: " + trainingDto);
    Training training = new Training();
    training.setTrainingName(trainingDto.trainingName());
    training.setTrainingDate(trainingDto.trainingDate());
    training.setTrainingDuration(trainingDto.trainingDuration());
    //training.setTrainingType(trainingDto.trainingTypeId()); ДОбавить тип
    trainingRepository.create(training);
    log.debug("Training created: " + training);
  }
}
