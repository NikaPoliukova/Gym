package upskill.client;


import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.*;
import upskill.entity.Training;

import java.time.LocalDate;
import java.util.List;


@Validated
@FeignClient(name = "gym-service")
public interface GymServiceClient {

  @PostMapping("/registration/trainee")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainee")
  Principal traineeRegistration(@RequestBody TraineeRegistration traineeRegistration);

  @PostMapping("/registration/trainer")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainer")
  Principal trainerRegistration(@RequestBody TrainerRegistration trainerRegistration);

  //trainee controller
  @GetMapping("/trainees/trainee")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get Trainee", description = "Get Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  TraineeResponse getTrainee(@RequestParam("username") String username);

  @PutMapping("/trainees/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee profile", description = "Update Trainee profile")
  @SecurityRequirement(name = "Bearer Authentication")
  TraineeUpdateResponse updateTrainee(@RequestParam("username") String username,
                                      @RequestParam("firstName") String firstName,
                                      @RequestParam("lastName") String lastName,
                                      @RequestParam(value = "dateOfBirth", required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                      @RequestParam(value = "address", required = false) String address,
                                      @RequestParam("isActive") boolean isActive);

  @Operation(summary = "Delete user", description = "Delete Trainee by username")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/trainees")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteTrainee(@RequestParam("username") String username);

  @GetMapping("/trainees/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find not assigned active trainers for a Trainee",
      description = "Find not assigned active trainers for a Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") String username);

  @PutMapping("/trainees/setting/trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee's trainers", description = "Update Trainee's trainers")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainerDtoForTrainee> updateTrainerList(@RequestParam("username") String username,
                                               @RequestParam("trainingDate") String trainingDate,
                                               @RequestParam("trainingName") String trainingName,
                                               @RequestBody List<TrainersDtoList> list);

  @GetMapping("/trainees/trainings")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find Trainee's trainings", description = "Find Trainee's trainings")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainingTraineeResponse> findTrainingsList(@RequestParam("username") String username,
                                                  @RequestParam(value = "periodFrom", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                  @RequestParam(value = "periodTo", required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                  @RequestParam(value = "trainerName", required = false) String trainerName,
                                                  @RequestParam(value = "trainingType", required = false) String trainingType);

  @PatchMapping("/trainees/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "toggle a Trainee's profile", description = "toggle a Trainee's profile")
  @SecurityRequirement(name = "Bearer Authentication")
  void toggleActivation(@RequestParam("username") String username,
                        @RequestParam("active") boolean isActive);

  //trainer controller
  @GetMapping("/api/v1/trainers/trainer")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Get Trainer by username")
  TrainerResponse getTrainer(@RequestParam("username") String username);


  @PutMapping("/trainers/trainer/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainer's profile")
  TrainerUpdateResponse updateTrainer(@RequestParam("username") String username,
                                      @RequestParam("firstName") String firstName,
                                      @RequestParam("lastName") String lastName,
                                      @RequestParam("specialization") String specialization,
                                      @RequestParam("isActive") boolean isActive);


  @GetMapping("/trainers/trainer/trainings")
  @ApiOperation("Find Trainer's trainings")
  List<TrainingTrainerResponse> findTrainerTrainingsList(@RequestParam("username") String username,
                                                         @RequestParam(value = "periodFrom", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodFrom,
                                                         @RequestParam(value = "periodTo", required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodTo,
                                                         @RequestParam(value = "traineeName", required = false)
                                                         String traineeName);

  @PatchMapping("/trainers/trainer/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate Trainer's profile")
  void toggleActivationTrainer(@RequestParam("username") String username,
                               @RequestParam("active") boolean isActive);

  //training controller

  @PostMapping("/trainings/new-training")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Save training")
  Training saveTraining(@RequestBody TrainingRequest trainingRequest);

  @PostMapping("/trainings/training")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("delete training")
  void deleteTraining(@RequestBody TrainingRequestDto trainingRequest);

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/trainings/training-types")
  List<TrainingTypeResponse> getTrainingTypes();

  //userController

  @PutMapping("/users/user/setting/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Change login")
  void changeLogin(@RequestParam("username") String username,
                   @RequestParam("oldPassword") String oldPassword,
                   @RequestParam("newPassword") String newPassword);
}
