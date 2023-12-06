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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;



@Validated
@FeignClient(name = "gym", url = "${services.gym.url}")
public interface GymServiceClient {

  @PostMapping("/registration/trainee")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainee")
  public Principal traineeRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                       @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                       @RequestParam(required = false) @Size(min = 2) String address,
                                       @RequestParam(required = false)
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth);

  @PostMapping("/registration/trainer")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Register a new trainer")
  Principal trainerRegistration(@RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                @RequestParam("specialization") @NotBlank String specialization);

  //trainee controller
  @GetMapping("/trainees/trainee")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get Trainee", description = "Get Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  TraineeResponse getTrainee(@RequestParam("username") @NotBlank String username);

  @PutMapping("/trainees/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee profile", description = "Update Trainee profile")
  @SecurityRequirement(name = "Bearer Authentication")
  TraineeUpdateResponse updateTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                      @RequestParam("firstName") @Size(min = 2, max = 30) String firstName,
                                      @RequestParam("lastName") @Size(min = 2, max = 30) String lastName,
                                      @RequestParam(required = false)
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                      @RequestParam(value = "address", required = false) @Size(min = 5) String address,
                                      @RequestParam("isActive") @NotNull boolean isActive);

  @Operation(summary = "Delete user", description = "Delete Trainee by username")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping("/trainees")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username);

  @GetMapping("/trainees/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find not assigned active trainers for a Trainee",
      description = "Find not assigned active trainers for a Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") @NotBlank
                                                   @Size(min = 2, max = 60) String username);

  @PutMapping("/trainees/setting/trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee's trainers", description = "Update Trainee's trainers")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainerDtoForTrainee> updateTrainerList(@RequestParam("username") @NotBlank
                                               @Size(min = 2, max = 60) String username,
                                               @RequestParam("trainingDate") @NotBlank String trainingDate,
                                               @RequestParam("trainingName") @NotBlank String trainingName,
                                               @RequestBody @NotEmpty List<TrainersDtoList> list);

  @GetMapping("/trainees/trainings")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find Trainee's trainings", description = "Find Trainee's trainings")
  @SecurityRequirement(name = "Bearer Authentication")
  List<TrainingTraineeResponse> findTrainingsList(@RequestParam("username") @NotBlank @Size(min = 2, max = 60)
                                                  String username,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                                                  @RequestParam(required = false)
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                                                  @RequestParam(required = false) String trainerName,
                                                  @RequestParam(required = false) String trainingType);

  @PatchMapping("/trainees/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "toggle a Trainee's profile", description = "toggle a Trainee's profile")
  @SecurityRequirement(name = "Bearer Authentication")
  void toggleActivation(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                        @RequestParam("active") @NotNull boolean isActive);

  //trainer controller
  @GetMapping("/trainers/trainer")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Get Trainer by username")
  TrainerResponse getTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username);


  @PutMapping("/trainers/trainer/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update Trainer's profile")
  TrainerUpdateResponse updateTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                      @RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                                      @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                                      @RequestParam("specialization") @NotBlank String specialization,
                                      @RequestParam("isActive") @NotNull boolean isActive);


  @GetMapping("/trainers/trainer/trainings")
  @ApiOperation("Find Trainer's trainings")
  List<TrainingTrainerResponse> findTrainerTrainingsList(@RequestParam("username") @NotBlank
                                                         @Size(min = 2, max = 60) String username,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodFrom,
                                                         @RequestParam(required = false)
                                                         @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                         LocalDate periodTo,
                                                         @RequestParam(required = false) String traineeName);

  @PatchMapping("/trainers/trainer/toggle-activation")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Activate or deactivate Trainer's profile")
  void toggleActivationTrainer(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                               @RequestParam("active") @NotNull boolean isActive);

  //training controller

  @PostMapping("/trainings/new-training")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Save training")
  void saveTraining(@RequestBody @Valid TrainingRequest trainingRequest);

  @PostMapping("/trainings/training")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("delete training")
  void deleteTraining(@RequestBody @Valid TrainingRequestDto trainingRequest);

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/trainings/training-types")
  List<TrainingTypeResponse> getTrainingTypes();

  //userController

  @PutMapping("/users/user/setting/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Change login")
  void changeLogin(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                   @RequestParam("oldPassword") @NotBlank @Size(min = 10, max = 10) String oldPassword,
                   @RequestParam("newPassword") @NotBlank @Size(min = 10, max = 10, message = "New password must" +
                       " be 10 characters") String newPassword);
}
