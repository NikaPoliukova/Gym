package com.epam.upskill.controller;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.security.Principal;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/registration")
@Api(tags = "Registration")
public class RegistrationController {

  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @PostMapping("/trainee")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Register a new trainee")
  public Principal traineeRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                       @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                       @RequestParam(required = false) @Size(min = 2) String address,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth) {
    TraineeRegistration traineeRegistration = new TraineeRegistration(firstName, lastName, address, dateOfBirth);
    var trainee = traineeService.saveTrainee(traineeRegistration);
    return new Principal(trainee.getUsername(), trainee.getPassword());
  }

  @PostMapping("/trainer")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Register a new trainer")
  public Principal trainerRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                       @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                       @RequestParam("specialization") @NotBlank String specialization) {
    TrainerRegistration trainerRegistration = new TrainerRegistration(firstName, lastName, specialization);
    var trainer = trainerService.saveTrainer(trainerRegistration);
    return new Principal(trainer.getUsername(), trainer.getPassword());
  }
}
