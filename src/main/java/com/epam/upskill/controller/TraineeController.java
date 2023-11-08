package com.epam.upskill.controller;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.exception.TraineeNotFoundException;
import com.epam.upskill.exception.UpdateTraineeException;
import com.epam.upskill.exception.UpdateTrainerListException;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainingService;
import com.epam.upskill.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/trainees")
public class TraineeController {
  private final UserService userService;
  private final TraineeService traineeService;
  private final TraineeConverter converter;
  private final TrainerConverter trainerConverter;
  private final TrainingConverter trainingConverter;
  private final TrainingService trainingService;

  @GetMapping("/trainee")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Get Trainee by username")
  public TraineeResponse getTrainee(@RequestParam("username") @NotBlank String username) {
    try {
      var trainee = traineeService.findByUsername(username);
      return converter.toTraineeResponse(trainee, traineeService);
    } catch (TraineeNotFoundException ex) {
      throw new TraineeNotFoundException(username);
    }
  }

  @PutMapping("/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainee profile")
  public TraineeUpdateResponse updateTrainee(@RequestParam("username") @NotBlank String username,
                                             @RequestParam("firstName") String firstName,
                                             @RequestParam("lastName") String lastName,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                             @RequestParam(value = "address", required = false) String address,
                                             @RequestParam("isActive") boolean isActive) {
    try {
      TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest(username, firstName, lastName, dateOfBirth, address, isActive);
      var trainee = traineeService.updateTrainee(traineeUpdateRequest);
      return converter.toTraineeUpdateResponse(trainee, traineeService);
    } catch (UpdateTraineeException ex) {
      throw new UpdateTraineeException(username);
    }
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Delete Trainee by username")
  public void deleteTrainee(@RequestParam("username") @NotBlank String username) {
    try {
      var trainee = traineeService.findByUsername(username);
      userService.delete(trainee.getId());
    } catch (TraineeNotFoundException ex) {
      throw new TraineeNotFoundException(username);
    }
  }

  @GetMapping("/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Find not assigned active trainers for a Trainee")
  public List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") @NotBlank String username) {
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(username);
    return trainerConverter.toTrainerDtoForTrainee(trainerList);
  }

  @PutMapping("/trainers")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainee's trainers")
  public List<TrainerDtoForTrainee> updateTrainerList(
      @RequestParam("username") @NotBlank String username,
      @RequestParam("trainingDate") @NotBlank String trainingDate,
      @RequestParam("trainingName") @NotBlank String trainingName,
      @RequestBody @NotEmpty List<TrainersDtoList> list) {
    try {
      return trainingService.updateTraineeTrainerList(new UpdateTraineeTrainerDto(username, trainingDate, trainingName, list));
    } catch (UpdateTrainerListException ex) {
      throw new UpdateTrainerListException(username);
    }
  }

  @GetMapping("/trainings")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Find Trainee's trainings")
  public List<TrainingTraineeResponse> findTrainingsList(@RequestParam("username") @NotBlank String username,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                         @RequestParam(required = false) String trainerName,
                                                         @RequestParam(required = false) String trainingType) {
    if (periodFrom != null && periodTo != null && periodFrom.isAfter(periodTo)) {
      throw new IllegalArgumentException("periodFrom must be before or equal to periodTo");
    }

    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(new TrainingTraineeRequest
        (username, periodFrom, periodTo, trainerName, trainingType));
    return trainingConverter.toTrainingResponse(trainingsList);
  }

  @PatchMapping("/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate a Trainee's profile")
  public void toggleActivation(@RequestParam("username") @NotBlank String username,
                               @RequestParam("active") @NotBlank boolean isActive) {
    try {
      var trainee = traineeService.findByUsername(username);
      traineeService.toggleProfileActivation(trainee.getId(), isActive);
    } catch (TraineeNotFoundException ex) {
      throw new TraineeNotFoundException(username);
    }
  }
}
