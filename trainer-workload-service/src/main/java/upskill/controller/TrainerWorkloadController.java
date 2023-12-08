package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;
import upskill.entity.TrainerTraining;
import upskill.service.TrainerWorkloadService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Api(tags = "Trainings")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/trainer/workload")
public class TrainerWorkloadController {
  private final TrainerWorkloadService workloadService;

  @PostMapping("/new-training")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Save training")
  public void saveTraining(@Valid @RequestBody TrainerTrainingDtoForSave trainingDto) {
    var trainingRequest = convertToTrainingRequest(trainingDto);
    workloadService.save(trainingRequest);
  }


  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("delete training")
  public void deleteTraining(@Valid @RequestBody TrainingRequestDto trainingDto) {
    var trainingRequest = convertToTrainingRequest(trainingDto);
    workloadService.delete(trainingRequest);
  }

  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  Integer getTrainerWorkload(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @RequestParam("periodFrom")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                             @RequestParam("periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                             @RequestParam(value = "trainingType", required = false) @Size(min = 2) String trainingType) {
    return workloadService.getTrainerWorkload(trainerUsername, periodFrom, periodTo, trainingType);
  }

  private static TrainerTraining convertToTrainingRequest(TrainingRequestDto trainingDto) {
    return TrainerTraining.builder()
        .trainerUsername(trainingDto.getTrainerUsername())
        .trainingName(trainingDto.getTrainingName())
        .trainingDate(trainingDto.getTrainingDate())
        .trainingType(trainingDto.getTrainingType())
        .trainingDuration(trainingDto.getDuration()).build();
  }

  private static TrainerTraining convertToTrainingRequest(TrainerTrainingDtoForSave trainingDto) {
    return TrainerTraining.builder()
        .trainerUsername(trainingDto.getTrainerUsername())
        .firstName(trainingDto.getFirstName())
        .lastName(trainingDto.getLastName())
        .trainingName(trainingDto.getTrainingName())
        .trainingDate(trainingDto.getTrainingDate())
        .trainingType(trainingDto.getTrainingType())
        .trainingDuration(trainingDto.getDuration())
        .build();
  }

}
