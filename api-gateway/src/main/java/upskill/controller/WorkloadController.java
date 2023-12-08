package upskill.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import upskill.client.WorkloadClient;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainer/workload")
public class WorkloadController {
  private final WorkloadClient workloadClient;

  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping
  public Integer getTrainerWorkload(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @RequestParam("periodFrom")
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                             @RequestParam("periodTo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                             @RequestParam(value = "trainingType", required = false) @Size(min = 2) String trainingType) {
    return workloadClient.getTrainerWorkload(trainerUsername, periodFrom, periodTo, trainingType);
  }
}
