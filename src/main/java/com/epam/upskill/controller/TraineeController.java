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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

import static com.epam.upskill.util.UserUtils.getLocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainees")
public class TraineeController {
  private final UserService userService;
  private final TraineeService traineeService;
  private final TraineeConverter converter;
  private final TrainerConverter trainerConverter;
  private final TrainingConverter trainingConverter;
  private final TrainingService trainingService;


  @GetMapping("/trainee/profile")
  @ApiOperation("Find Trainee by username")
  public ResponseEntity<TraineeResponse> findTrainee(@RequestParam("username") @NotBlank String username) {
    var trainee = traineeService.findByUsername(username);
    var traineeResponse = converter.toTraineeResponse(trainee, traineeService);
    return ResponseEntity.ok(traineeResponse);
  }

  @PutMapping("/trainee/profile")
  @ApiOperation("Update Trainee profile")
  public ResponseEntity<TraineeUpdateResponse> updateTrainee(
      @RequestParam("username") @NotBlank String username,
      @RequestParam("firstName") String firstName,
      @RequestParam("lastName") String lastName,
      @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
      @RequestParam(value = "address", required = false) String address,
      @RequestParam("isActive") boolean isActive) {
    LocalDate localDate = getLocalDate(dateOfBirth);
    TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest(username, firstName, lastName, localDate,
        address, isActive);
    var trainee = traineeService.updateTrainee(traineeUpdateRequest);
    return ResponseEntity.ok(converter.toTraineeUpdateResponse(trainee, traineeService));
  }

  @DeleteMapping("/trainee")
  @ApiOperation("Delete Trainee by username")
  public ResponseEntity<String> deleteTrainee(@RequestParam("username") @NotBlank String username) {
    var trainee = traineeService.findByUsername(username);
    userService.delete(trainee.getId());
    return ResponseEntity.ok("200 OK");
  }

  @GetMapping("/trainee/not-active-trainers")
  @ApiOperation("Find not assigned active trainers for a Trainee")
  public ResponseEntity<List<TrainerDtoForTrainee>> findNotActiveTrainers(
      @RequestParam("username") @NotBlank String username) {
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(username);
    trainerConverter.toTrainerDtoForTrainee(trainerList);
    return ResponseEntity.ok(trainerConverter.toTrainerDtoForTrainee(trainerList));
  }

  @PutMapping("/trainee/trainers")
  @ApiOperation("Update Trainee's trainers")
  public ResponseEntity<List<TrainerDtoForTrainee>> updateTraineeTrainerList(
      @RequestParam("username") @NotBlank String username,
      @RequestParam("trainingDate") @NotBlank String trainingDate,
      @RequestParam("trainingName") @NotBlank String trainingName,
      @RequestBody List<TrainersDtoList> list) {
    return ResponseEntity.ok(trainingService.updateTraineeTrainerList(username, trainingDate, trainingName, list));
  }

  @GetMapping("/trainee/trainings")
  @ApiOperation("Find Trainee's trainings")
  public ResponseEntity<List<TrainingTraineeResponse>> findTraineeTrainingsList(
      @RequestParam("username") @NotBlank String username,
      @RequestParam(required = false) String periodFrom,
      @RequestParam(required = false) String periodTo,
      @RequestParam(required = false) String trainerName,
      @RequestParam(required = false) String trainingType) {
    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(username, periodFrom,
        periodTo, trainerName, trainingType);
    return ResponseEntity.ok(trainingConverter.toTrainingResponse(trainingsList));
  }


  @PatchMapping("/trainer/toggle-activation")
  @ApiOperation("Activate or deactivate a Trainee's profile")
  public ResponseEntity<Void> toggleActivation(@RequestParam("username") @NotBlank String username,
                                               @RequestParam("active") @NotBlank boolean isActive) {
    var trainee = traineeService.findByUsername(username);
    traineeService.toggleProfileActivation(trainee.getId(), isActive);
    return ResponseEntity.ok().build();
  }
}
