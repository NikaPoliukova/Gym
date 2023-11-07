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

import javax.validation.constraints.NotBlank;

import static com.epam.upskill.util.UserUtils.getLocalDate;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/registration")
@Api(tags = "Registration")
public class RegistrationController {
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @PostMapping("/trainee")
  @ApiOperation("Register a new trainee")
  public ResponseEntity<Principal> traineeRegistration(@RequestParam("firstName") @NotBlank String firstName,
                                                       @RequestParam("lastName") @NotBlank String lastName,
                                                       @RequestParam(required = false) String address,
                                                       @RequestParam(required = false) String dateOfBirth) {
    TraineeRegistration traineeRegistration = new TraineeRegistration(firstName, lastName, address,
        getLocalDate(dateOfBirth));
    var trainee = traineeService.saveTrainee(traineeRegistration);
    return ResponseEntity.ok(new Principal(trainee.getUsername(), trainee.getPassword()));
  }

  @PostMapping("/trainer")
  @ApiOperation("Register a new trainer")
  public ResponseEntity<Principal> trainerRegistration(@RequestParam("firstName") @NotBlank String firstName,
                                                       @RequestParam("lastName") @NotBlank String lastName,
                                                       @RequestParam("specialization") @NotBlank String specialization) {
    TrainerRegistration trainerRegistration = new TrainerRegistration(firstName, lastName, specialization);
    var trainer = trainerService.saveTrainer(trainerRegistration);
    return ResponseEntity.ok(new Principal(trainer.getUsername(), trainer.getPassword()));
  }
}
