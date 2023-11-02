package com.epam.upskill.controller;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.security.Principal;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//РАБОТАEТ
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Api(tags = "Registration")
public class RegistrationController {
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @PostMapping("/trainee-registration")
  @ApiOperation("Register a new trainee")
  public ResponseEntity<Principal> traineeRegistration(@RequestParam String firstName,
                                                       @RequestParam String lastName,
                                                       @RequestParam(required = false) String address,
                                                       @RequestParam(required = false) String dateOfBirth) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
    TraineeRegistration traineeRegistration = new TraineeRegistration(firstName, lastName, address, localDate);
    var trainee = traineeService.saveTrainee(traineeRegistration);
    var principal = new Principal(trainee.getUsername(), trainee.getPassword());
    return ResponseEntity.ok(principal);
  }

  @PostMapping("/trainer-registration")
  @ApiOperation("Register a new trainer")
  public ResponseEntity<Principal> trainerRegistration(@RequestParam String firstName,
                                                       @RequestParam String lastName,
                                                       @RequestParam String specialization) {
    TrainerRegistration trainerRegistration = new TrainerRegistration(firstName, lastName, specialization);
    var trainer = trainerService.saveTrainer(trainerRegistration);
    var principal = new Principal(trainer.getUsername(), trainer.getPassword());
    return ResponseEntity.ok(principal);
  }
}
