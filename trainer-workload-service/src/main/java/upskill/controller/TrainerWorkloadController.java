package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.entity.TrainerTraining;
import upskill.service.TrainerWorkloadService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Api(tags = "Trainings")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/trainer/workload")
public class TrainerWorkloadController {
  private final TrainerWorkloadService workloadService;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/new-training")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Save training")
  public void saveTraining(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                           @RequestParam("firstName") @NotBlank @Size(min = 2, max = 30) String firstName,
                           @RequestParam("lastName") @NotBlank @Size(min = 2, max = 30) String lastName,
                           @RequestParam("trainingName") @NotBlank @Size(min = 2, max = 100) String trainingName,
                           @RequestParam("trainingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate,
                           @RequestParam("trainingType") @NotBlank String trainingType,
                           @RequestParam("trainingDuration") @NotNull int duration) {
    var trainingRequest = TrainerTraining.builder()
        .trainerUsername(trainerUsername)
        .firstName(firstName)
        .lastName(lastName)
        .trainingName(trainingName)
        .trainingDate(trainingDate)
        .trainingType(trainingType)
        .trainingDuration(duration)
        .build();
    workloadService.save(trainingRequest);
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("delete training")
  public void deleteTraining(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @RequestParam("trainingName") @NotBlank @Size(min = 2, max = 100) String trainingName,
                             @RequestParam("trainingDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate trainingDate,
                             @RequestParam("trainingType") @NotBlank String trainingType,
                             @RequestParam("trainingDuration") @NotNull int duration) {
    var trainingRequest = TrainerTraining.builder()
        .trainerUsername(trainerUsername)
        .trainingName(trainingName)
        .trainingDate(trainingDate)
        .trainingType(trainingType)
        .trainingDuration(duration).build();
    workloadService.delete(trainingRequest);
  }

  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  Integer getTrainerWorkload(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                             @RequestParam(required = false) @Size(min = 2) String trainingType) {
    return workloadService.getTrainerWorkload(trainerUsername, periodFrom, periodTo, trainingType);
  }
}
