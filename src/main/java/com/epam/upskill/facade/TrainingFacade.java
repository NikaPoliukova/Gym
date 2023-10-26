package com.epam.upskill.facade;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TrainingFacade {
  private final TrainingService trainingService;

  public Optional<Training> getTrainingById(long trainingId) {
    return trainingService.getTrainingById(trainingId);
  }

  public void createTraining(TrainingDto trainingDto) {
    trainingService.createTraining(trainingDto);
  }
}
