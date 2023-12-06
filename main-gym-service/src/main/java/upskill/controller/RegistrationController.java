package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.TraineeRegistration;
import upskill.dto.TrainerRegistration;
import upskill.dto.Principal;
import upskill.service.TraineeService;
import upskill.service.TrainerService;

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
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainee")
  public Principal traineeRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                       @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                       @RequestParam(required = false) @Size(min = 2) String address,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth) {
    TraineeRegistration traineeRegistration = new TraineeRegistration(firstName, lastName, address, dateOfBirth);
    return traineeService.saveTrainee(traineeRegistration);
  }

  @PostMapping("/trainer")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainer")
  public Principal trainerRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                       @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                       @RequestParam("specialization") @NotBlank String specialization) {
    TrainerRegistration trainerRegistration = new TrainerRegistration(firstName, lastName, specialization);
    return trainerService.saveTrainer(trainerRegistration);
  }
}