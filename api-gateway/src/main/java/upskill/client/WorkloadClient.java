package upskill.client;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;

import javax.validation.Valid;
import java.time.LocalDate;

@Validated
@FeignClient(name = "workload-service")
public interface WorkloadClient {

  @PostMapping(value = "/api/v1/trainer/workload/new-training")
  void saveTraining(@Valid @RequestBody TrainerTrainingDtoForSave trainingDto);


  @PostMapping("/api/v1/trainer/workload")
  @ApiOperation("delete training")
  void deleteTraining(@Valid @RequestBody TrainingRequestDto trainingDto);


  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/api/v1/trainer/workload")
  Integer getTrainerWorkload(@RequestParam("trainerUsername") String trainerUsername,
                             @RequestParam("periodFrom") LocalDate periodFrom,
                             @RequestParam("periodFrom") LocalDate periodTo,
                             @RequestParam(value = "trainingType",required = false ) String trainingType);
}

