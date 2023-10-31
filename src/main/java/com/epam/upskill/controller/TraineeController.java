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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

  @GetMapping("/trainee-profile")
  public ResponseEntity<TraineeResponse> findTraineeProfile(@RequestParam String username,
                                                            @RequestParam String password) {
    var trainee = traineeService.findByUsernameAndPassword(username, password);
    List<TrainerDtoForTrainee> listTrainers = traineeService.findTrainersForTrainee(trainee.getId());
    //TODO converter.toTraineeResponse(trainee);
    var traineeResponse = new TraineeResponse(trainee.getFirstName(), trainee.getLastName(), trainee.getDateOfBirth(),
        trainee.getAddress(), trainee.isActive(), listTrainers);
    return ResponseEntity.ok(traineeResponse);
  }

  @PutMapping("/trainee")
  public ResponseEntity<TraineeUpdateResponse> updateTraineeProfile(@RequestBody TraineeUpdateRequest request) {
    var trainee = traineeService.findByUsername(request.username());
    //TODO проверить конвертер
    var traineeUpdateResponse = converter.toTraineeUpdateResponse(trainee);
    //TODO нету листа тренеров
    return ResponseEntity.ok(traineeUpdateResponse);
  }

  @DeleteMapping("/trainee")
  public ResponseEntity<String> deleteTraineeProfile(@RequestParam String username,
                                                     @RequestParam String password) {
    var trainee = traineeService.findByUsernameAndPassword(username, password);
    userService.delete(trainee.getId());
    return ResponseEntity.ok("200 OK");
  }

  @GetMapping("/trainee/not-active-trainers")
  public ResponseEntity<List<TrainerDtoForTrainee>> findNotAssignedActiveTrainers(@RequestParam String username,
                                                                                  @RequestParam String password) {
    var trainee = traineeService.findByUsernameAndPassword(username, password);
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(trainee.getId());
    trainerConverter.toTrainerDtoForTrainee(trainerList);
    return ResponseEntity.ok(trainerConverter.toTrainerDtoForTrainee(trainerList));
  }

  @PutMapping("/trainee/trainers")
  public ResponseEntity<List<TrainerDtoForTrainee>> updateTraineeTrainerList(@RequestBody TraineeTrainerListRequest request) {
//TODO реализовать логику обновления
    List<TrainerDtoForTrainee> updatedTrainersList = new ArrayList<>();
    return ResponseEntity.ok(updatedTrainersList);
  }

  @GetMapping("/trainee/trainings-list")
  public ResponseEntity<List<TrainingTraineeResponse>> findTraineeTrainingsList(@RequestParam String username,
                                                                                @RequestParam String password,
                                                                                @RequestParam(required = false) String periodFrom,
                                                                                @RequestParam(required = false) String periodTo,
                                                                                @RequestParam(required = false) String trainerName,
                                                                                @RequestParam(required = false) String trainingType) {
    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(username, password, periodFrom,
        periodTo, trainerName, trainingType);
    return ResponseEntity.ok(trainingConverter.toTrainingResponse(trainingsList));
  }

  @PatchMapping("/activate-deactivate-trainee")
  public ResponseEntity<Void> activateDeactivateTrainee(@RequestParam String username, @RequestParam String password,
                                                        @RequestParam boolean isActive) {
    var trainee = traineeService.findByUsernameAndPassword(username, password);
    traineeService.toggleProfileActivation(trainee.getId());
    return ResponseEntity.ok().build();
  }
}
