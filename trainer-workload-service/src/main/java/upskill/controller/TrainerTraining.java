package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.service.TrainingSummaryService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Api(tags = "Trainings")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/trainer/workload")
public class TrainerTraining {
  private final TrainingSummaryService trainingSummaryService;

  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  Integer getTrainerWorkload(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @RequestParam("year") @Min(2000) @Max(2028) int year,
                             @RequestParam("month") @Min(1) @Max(12) int month) {
    return trainingSummaryService.getTrainerWorkload(trainerUsername, year, month);
  }
}
