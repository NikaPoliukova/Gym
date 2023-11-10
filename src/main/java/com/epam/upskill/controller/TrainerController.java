package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/trainers/trainer")
@Validated
public class TrainerController {

  private final TrainerService trainerService;
  private final TrainerConverter converter;
  private final TrainingService trainingService;
  private final TrainingConverter trainingConverter;


  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Get Trainer by username")
  public TrainerResponse getTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username) {
    var trainer = trainerService.findByUsername(username);
    return converter.toTrainerResponse(trainer, trainerService);
  }

  @PutMapping("/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainer's profile")
  public TrainerUpdateResponse updateTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                             @RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                             @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                             @RequestParam("specialization") @NotBlank String specialization,
                                             @RequestParam("isActive") @NotNull boolean isActive) {
    var trainer = trainerService.update(new TrainerUpdateRequest(username, firstName, lastName, specialization, isActive));
    return converter.toTrainerUpdateResponse(trainer, trainerService);
  }

  @GetMapping("/trainings")
  @ApiOperation("Find Trainer's trainings")
  public List<TrainingTrainerResponse> findTrainerTrainingsList(@RequestParam("username") @NotBlank
                                                                  @Size(min = 2, max = 60)String username,
                                                                @RequestParam(required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                LocalDate periodFrom,
                                                                @RequestParam(required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                LocalDate periodTo,
                                                                @RequestParam(required = false) String traineeName) {
    List<Training> trainingsList = trainingService.findTrainerTrainings(new TrainingTrainerRequest
        (username, periodFrom, periodTo, traineeName));
    return trainingConverter.toTrainerTrainingResponse(trainingsList);
  }

  @PatchMapping("/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate Trainer's profile")
  public void toggleActivation(@RequestParam("username") @NotBlank @Size(min = 2, max = 60)String username,
                               @RequestParam("active") @NotNull boolean isActive) {
    var trainer = trainerService.findByUsername(username);
    trainerService.toggleProfileActivation(trainer.getId(), isActive);
  }
}
