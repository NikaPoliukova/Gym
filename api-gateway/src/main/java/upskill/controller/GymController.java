package upskill.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import upskill.client.GymServiceClient;
import upskill.client.WorkloadClient;
import upskill.dto.*;
import upskill.entity.Training;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class GymController {
  private final GymServiceClient gymServiceClient;
  private final WorkloadClient workloadClient;

  @PostMapping("/registration/trainee")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainee")
  public Principal traineeRegistration(@Valid @RequestBody TraineeRegistration traineeRegistration) {
    return gymServiceClient.traineeRegistration(traineeRegistration);
  }

  @PostMapping("/registration/trainer")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainer")
  public Principal trainerRegistration(@Valid @RequestBody TrainerRegistration trainerRegistration) {
    return gymServiceClient.trainerRegistration(trainerRegistration);
  }

  //trainee controller
  @GetMapping("/trainees/trainee")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get Trainee", description = "Get Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  public TraineeResponse getTrainee(@RequestParam("username") @NotBlank String username) {
    return gymServiceClient.getTrainee(username);
  }

  @PutMapping("/trainees/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee profile", description = "Update Trainee profile")
  @SecurityRequirement(name = "Bearer Authentication")
  TraineeUpdateResponse updateTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                      @RequestParam("firstName") @Size(min = 2, max = 30) String firstName,
                                      @RequestParam("lastName") @Size(min = 2, max = 30) String lastName,
                                      @RequestParam(value = "dateOfBirth", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                      @RequestParam(value = "address", required = false) @Size(min = 5) String address,
                                      @RequestParam("isActive") @NotNull boolean isActive) {
    return gymServiceClient.updateTrainee(username, firstName, lastName, dateOfBirth, address, isActive);
  }

  @Operation(summary = "Delete user", description = "Delete Trainee by username")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/trainees")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username) {
    gymServiceClient.deleteTrainee(username);
  }

  @GetMapping("/trainees/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find not assigned active trainers for a Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  public List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") @NotBlank
                                                   @Size(min = 2, max = 60) String username) {
    return gymServiceClient.findNotActiveTrainers(username);
  }

  @PutMapping("/trainees/setting/trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee's trainers", description = "Update Trainee's trainers")
  @SecurityRequirement(name = "Bearer Authentication")
  public List<TrainerDtoForTrainee> updateTrainerList(@RequestParam("username") @NotBlank
                                               @Size(min = 2, max = 60) String username,
                                               @RequestParam("trainingDate") @NotBlank String trainingDate,
                                               @RequestParam("trainingName") @NotBlank String trainingName,
                                               @RequestBody @NotEmpty List<TrainersDtoList> list) {
    return gymServiceClient.updateTrainerList(username, trainingDate, trainingName, list);
  }

  @GetMapping("/trainees/trainings")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find Trainee's trainings", description = "Find Trainee's trainings")
  @SecurityRequirement(name = "Bearer Authentication")
  public List<TrainingTraineeResponse> findTrainingsList(@RequestParam("username") @NotBlank @Size(min = 2, max = 60)
                                                  String username,
                                                  @RequestParam(value = "periodFrom", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                  @RequestParam(value = "periodTo", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                  @RequestParam(value = "trainerName", required = false)
                                                  String trainerName,
                                                  @RequestParam(value = "trainingType", required = false)
                                                  String trainingType) {
    return gymServiceClient.findTrainingsList(username, periodFrom, periodTo, trainerName, trainingType);
  }

  @PatchMapping("/trainees/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "toggle a Trainee's profile", description = "toggle a Trainee's profile")
  @SecurityRequirement(name = "Bearer Authentication")
  public void toggleActivation(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                        @RequestParam("active") @NotNull boolean isActive) {

    gymServiceClient.toggleActivation(username, isActive);
  }

  //trainer controller
  @GetMapping("/api/v1/trainers/trainer")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Get Trainer by username")
  public TrainerResponse getTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username) {
    return gymServiceClient.getTrainer(username);
  }


  @PutMapping("/trainers/trainer/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainer's profile")
  TrainerUpdateResponse updateTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                      @RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                      @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                      @RequestParam("specialization") @NotBlank String specialization,
                                      @RequestParam("isActive") @NotNull boolean isActive) {
    return gymServiceClient.updateTrainer(username, firstName, lastName, specialization, isActive);
  }


  @GetMapping("/trainers/trainer/trainings")
  @ApiOperation("Find Trainer's trainings")
  public List<TrainingTrainerResponse> findTrainerTrainingsList(@RequestParam("username") @NotBlank
                                                         @Size(min = 2, max = 60) String username,
                                                         @RequestParam(value = "periodFrom", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodFrom,
                                                         @RequestParam(value = "periodTo", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodTo,
                                                         @RequestParam(value = "traineeName", required = false)
                                                         String traineeName) {
    return gymServiceClient.findTrainerTrainingsList(username, periodFrom, periodTo, traineeName);
  }

  @PatchMapping("/trainers/trainer/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate Trainer's profile")
  public void toggleActivationTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                               @RequestParam("active") @NotNull boolean isActive) {
    gymServiceClient.toggleActivationTrainer(username, isActive);
  }

  //training controller

  @PostMapping("/trainings/new-training")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Save training")
  public void saveTraining(@RequestBody @Valid TrainingRequest trainingRequest) {
    var training = gymServiceClient.saveTraining(trainingRequest);
    workloadClient.saveTraining(convertToTrainerTrainingDtoForSave(training));
  }


  @PostMapping("/trainings/training")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("delete training")
  public void deleteTraining(@RequestBody @Valid TrainingRequestDto trainingRequest) {
    gymServiceClient.deleteTraining(trainingRequest);
    workloadClient.deleteTraining(trainingRequest);
  }

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/trainings/training-types")
  List<TrainingTypeResponse> getTrainingTypes() {
    return gymServiceClient.getTrainingTypes();
  }

  //userController

  @PutMapping("/users/user/setting/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Change login")
  public void changeLogin(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                   @RequestParam("oldPassword") @NotBlank @Size(min = 10, max = 10) String oldPassword,
                   @RequestParam("newPassword") @NotBlank @Size(min = 10, max = 10, message = "New password must" +
                       " be 10 characters") String newPassword) {
    gymServiceClient.changeLogin(username, oldPassword, newPassword);
  }

  private static TrainerTrainingDtoForSave convertToTrainerTrainingDtoForSave(Training training) {
    return TrainerTrainingDtoForSave.builder()
        .trainerUsername(training.getTrainer().getUsername())
        .firstName(training.getTrainer().getFirstName())
        .lastName(training.getTrainer().getLastName())
        .trainingName(training.getTrainingName())
        .trainingDate(training.getTrainingDate())
        .trainingType(training.getTrainingType().getTrainingTypeName().name())
        .duration(training.getTrainingDuration())
        .build();
  }
}
