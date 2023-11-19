package com.epam.upskill.controller;

import com.epam.upskill.converter.TraineeConverter;
import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    var trainee = traineeService.findByUsername(username);
    return converter.toTraineeResponse(trainee, traineeService);
  }

  @PutMapping("/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainee profile")
  public TraineeUpdateResponse updateTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                             @RequestParam("firstName") @Size(min = 2, max = 30) String firstName,
                                             @RequestParam("lastName") @Size(min = 2, max = 30) String lastName,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                             @RequestParam(value = "address", required = false) @Size(min = 5) String address,
                                             @RequestParam("isActive") @NotNull boolean isActive) {
    TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest(username, firstName, lastName, dateOfBirth,
        address, isActive);
    var trainee = traineeService.updateTrainee(traineeUpdateRequest);
    return converter.toTraineeUpdateResponse(trainee, traineeService);

  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Delete Trainee by username")
  public void deleteTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username) {
    var trainee = traineeService.findByUsername(username);
    userService.delete(trainee.getId());
  }

  @GetMapping("/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Find not assigned active trainers for a Trainee")
  public List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") @NotBlank
                                                          @Size(min = 2, max = 60) String username) {
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(username);
    return trainerConverter.toTrainerDtoForTrainee(trainerList);
  }

  @PutMapping("/setting/trainers")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainee's trainers")
  public List<TrainerDtoForTrainee> updateTrainerList(@RequestParam("username") @NotBlank
                                                      @Size(min = 2, max = 60) String username,
                                                      @RequestParam("trainingDate") @NotBlank String trainingDate,
                                                      @RequestParam("trainingName") @NotBlank String trainingName,
                                                      @RequestBody @NotEmpty List<TrainersDtoList> list) {
    var dto = new UpdateTraineeTrainerDto(username, trainingDate,
        trainingName, list);
    return trainingService.updateTraineeTrainerList(dto);

  }

  @GetMapping("/trainings")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Find Trainee's trainings")
  public List<TrainingTraineeResponse> findTrainingsList(@RequestParam("username") @NotBlank @Size(min = 2, max = 60)
                                                         String username,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                         @RequestParam(required = false) String trainerName,
                                                         @RequestParam(required = false) String trainingType) {

    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(new TrainingTraineeRequest
        (username, periodFrom, periodTo, trainerName, trainingType));
    return trainingConverter.toTrainingResponse(trainingsList);
  }

  @PatchMapping("/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate a Trainee's profile")
  public void toggleActivation(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                               @RequestParam("active") @NotNull boolean isActive) {
    var trainee = traineeService.findByUsername(username);
    traineeService.toggleProfileActivation(trainee.getId(), isActive);
  }
}
