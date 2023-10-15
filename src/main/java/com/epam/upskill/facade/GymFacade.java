package com.epam.upskill.facade;

import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GymFacade {

  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final TrainingService trainingService;
}
