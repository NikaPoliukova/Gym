package com.epam.upskill.facade;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @deprecated ()
 */
@RequiredArgsConstructor
@Service
@Deprecated(forRemoval = false, since = "2.0")
public class RegistrationFacade {
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  public void registration(TraineeRegistration traineeRegistration) {
    traineeService.saveTrainee(traineeRegistration);
  }

  public void registration(TrainerRegistration trainerRegistration) {
    trainerService.saveTrainer(trainerRegistration);
  }
}
