package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.Principal;
import upskill.dto.TraineeRegistration;
import upskill.dto.TrainerRegistration;
import upskill.service.TraineeService;
import upskill.service.TrainerService;

import javax.validation.Valid;

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
  public Principal traineeRegistration(@Valid @RequestBody TraineeRegistration traineeRegistration) {
    return traineeService.saveTrainee(traineeRegistration);
  }

  @PostMapping("/trainer")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainer")
  public Principal trainerRegistration(@Valid @RequestBody TrainerRegistration trainerRegistration) {
    return trainerService.saveTrainer(trainerRegistration);
  }
}
