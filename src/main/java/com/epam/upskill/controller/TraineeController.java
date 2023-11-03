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

import java.time.LocalDate;
import java.util.ArrayList;
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

//работает
  @GetMapping("/trainee-profile")
  public ResponseEntity<TraineeResponse> findTraineeProfile(@RequestParam("username") String username) {
    var trainee = traineeService.findByUsername(username);
    var traineeResponse = converter.toTraineeResponse(trainee, traineeService);
    return ResponseEntity.ok(traineeResponse);
  }
  //работает
  @PutMapping("/trainee")
  public ResponseEntity<TraineeUpdateResponse> updateTraineeProfile(@RequestParam("username") String username,
                                                                    @RequestParam("firstName") String firstName,
                                                                    @RequestParam("lastName") String lastName,
                                                                    @RequestParam(value = "dateOfBirth", required = false) String dateOfBirth,
                                                                    @RequestParam(value = "address", required = false) String address,
                                                                    @RequestParam("isActive") boolean isActive) {
    LocalDate localDate = getLocalDate(dateOfBirth);
    TraineeUpdateRequest traineeUpdateRequest = new TraineeUpdateRequest(username, firstName, lastName, localDate,
        address, isActive);
    var trainee = traineeService.updateTrainee(traineeUpdateRequest);
    var traineeUpdateResponse = converter.toTraineeUpdateResponse(trainee,traineeService);
    return ResponseEntity.ok(traineeUpdateResponse);
  }
  //работает
  @DeleteMapping("/trainee")
  public ResponseEntity<String> deleteTraineeProfile(@RequestParam String username) {
    var trainee = traineeService.findByUsername(username);
    userService.delete(trainee.getId());
    return ResponseEntity.ok("200 OK");
  }

  @GetMapping("/trainee/not-active-trainers")
  public ResponseEntity<List<TrainerDtoForTrainee>> findNotAssignedActiveTrainers(@RequestParam String username) {
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(username);
    trainerConverter.toTrainerDtoForTrainee(trainerList);
    return ResponseEntity.ok(trainerConverter.toTrainerDtoForTrainee(trainerList));
  }

  @PutMapping("/trainee/trainers")
  public ResponseEntity<List<TrainerDtoForTrainee>> updateTraineeTrainerList(@RequestParam("username") String username,
                                                                             @RequestParam("trainingDate") String trainingDate,
                                                                             @RequestParam("trainingType") String trainingType,
                                                                             @RequestParam List<TrainersDtoList> list) {

    List<TrainerDtoForTrainee> updatedTrainersList = new ArrayList<>();
    return ResponseEntity.ok(updatedTrainersList);
  }

  @GetMapping("/trainee/trainings-list")
  public ResponseEntity<List<TrainingTraineeResponse>> findTraineeTrainingsList(@RequestParam String username,
                                                                                @RequestParam(required = false) String periodFrom,
                                                                                @RequestParam(required = false) String periodTo,
                                                                                @RequestParam(required = false) String trainerName,
                                                                                @RequestParam(required = false) String trainingType) {
    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(username, periodFrom,
        periodTo, trainerName, trainingType);
    //TODO Для каждого тренинга нужно засетать имя тренера
    return ResponseEntity.ok(trainingConverter.toTrainingResponse(trainingsList));
  }
  //работает
  @PatchMapping("/activate-deactivate-trainee")
  public ResponseEntity<Void> activateDeactivateTrainee(@RequestParam("username")String username,
                                                        @RequestParam("active") boolean isActive) {
    var trainee = traineeService.findByUsername(username);
    traineeService.toggleProfileActivation(trainee.getId(),isActive);
    return ResponseEntity.ok().build();
  }
}
