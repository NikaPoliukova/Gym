package upskill.client;


import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.TrainerTrainingDtoForSave;
import upskill.dto.TrainingRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Validated
@FeignClient(name ="workload",url = "${services.workload.url}")
public interface WorkloadClient {

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE,
      value = "/training")
  void saveTraining(@Valid @RequestBody TrainerTrainingDtoForSave trainingDto);


  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("delete training")
  void deleteTraining(@Valid @RequestBody TrainingRequestDto trainingDto);


  @ApiOperation("Get a summary duration")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  Integer getTrainerWorkload(@RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodFrom,
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodTo,
                             @RequestParam(required = false) @Size(min = 2) String trainingType);
}

