package com.epam.upskill.controller;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.security.Principal;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class RegistrationController {
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @PostMapping("/trainee-registration")
  public ResponseEntity<Principal> traineeRegistration(@RequestBody TraineeRegistration request) {
    var trainee = traineeService.saveTrainee(request);
    var principal = new Principal(trainee.getUsername(), trainee.getPassword());
    return ResponseEntity.ok(principal);
  }

  @PostMapping("/trainer-registration")
  public ResponseEntity<Principal> trainerRegistration(@RequestBody TrainerRegistration request) {
    var trainer = trainerService.saveTrainer(request);
    var principal = new Principal(trainer.getUsername(), trainer.getPassword());
    return ResponseEntity.ok(principal);
  }

}
