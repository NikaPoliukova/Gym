package upskill.controller;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.converter.TraineeConverter;
import upskill.converter.TrainerConverter;
import upskill.converter.TrainingConverter;
import upskill.dto.*;
import upskill.entity.Trainer;
import upskill.entity.Training;
import upskill.service.TraineeService;
import upskill.service.TrainingService;
import upskill.service.UserService;

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

  @Autowired
  private Counter customRequestsCounter;

  @Autowired
  private Timer customRequestLatencyTimer;

  @GetMapping("/trainee")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Get Trainee", description = "Get Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  public TraineeResponse getTrainee(@RequestParam("username") @NotBlank String username) {
    var trainee = traineeService.findByUsername(username);
    return converter.toTraineeResponse(trainee, traineeService);
  }

  @PutMapping("/trainee/setting/profile")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee profile", description = "Update Trainee profile")
  @SecurityRequirement(name = "Bearer Authentication")
  public TraineeUpdateResponse updateTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                                             @RequestParam("firstName") @Size(min = 2, max = 30) String firstName,
                                             @RequestParam("lastName") @Size(min = 2, max = 30) String lastName,
                                             @RequestParam(required = false)
                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
                                             @RequestParam(value = "address", required = false) @Size(min = 5) String address,
                                             @RequestParam("isActive") @NotNull boolean isActive) {
    var traineeUpdateRequest = new TraineeUpdateRequest(username, firstName, lastName, dateOfBirth,
        address, isActive);
    var trainee = traineeService.updateTrainee(traineeUpdateRequest);
    return converter.toTraineeUpdateResponse(trainee, traineeService);
  }

  @Operation(summary = "Delete user", description = "Delete Trainee by username")
  @SecurityRequirement(name = "Bearer Authentication")
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTrainee(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username) {
    var trainee = traineeService.findByUsername(username);
    userService.delete(trainee.getId());
  }

  @GetMapping("/not-active-trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find not assigned active trainers for a Trainee",
      description = "Find not assigned active trainers for a Trainee")
  @SecurityRequirement(name = "Bearer Authentication")
  public List<TrainerDtoForTrainee> findNotActiveTrainers(@RequestParam("username") @NotBlank
                                                          @Size(min = 2, max = 60) String username) {
    List<Trainer> trainerList = trainingService.findNotAssignedActiveTrainersToTrainee(username);
    return trainerConverter.toTrainerDtoForTrainee(trainerList);
  }

  @PutMapping("/setting/trainers")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Update Trainee's trainers", description = "Update Trainee's trainers")
  @SecurityRequirement(name = "Bearer Authentication")
  public List<TrainerDtoForTrainee> updateTrainerList(@RequestParam("username") @NotBlank
                                                      @Size(min = 2, max = 60) String username,
                                                      @RequestParam("trainingDate") @NotBlank String trainingDate,
                                                      @RequestParam("trainingName") @NotBlank String trainingName,
                                                      @RequestBody @NotEmpty List<TrainersDtoList> list) {
    var dto = new UpdateTraineeTrainerDto(username, trainingDate, trainingName, list);
    return trainingService.updateTraineeTrainerList(dto);
  }

  @GetMapping("/trainings")
  @ResponseStatus(HttpStatus.OK)
  @Operation(summary = "Find Trainee's trainings", description = "Find Trainee's trainings")
  @SecurityRequirement(name = "Bearer Authentication")
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
  @Operation(summary = "toggle a Trainee's profile", description = "toggle a Trainee's profile")
  @SecurityRequirement(name = "Bearer Authentication")
  public void toggleActivation(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                               @RequestParam("active") @NotNull boolean isActive) {
    customRequestsCounter.increment();
    Timer.Sample sample = Timer.start();
    var trainee = traineeService.findByUsername(username);
    traineeService.toggleProfileActivation(trainee.getId(), isActive);
    sample.stop(customRequestLatencyTimer);
  }
}
