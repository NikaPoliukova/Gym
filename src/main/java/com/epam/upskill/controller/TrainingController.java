package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Api(tags = "Trainings")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/trainings")
public class TrainingController {
  private final TrainingService trainingService;
  private final TrainingTypeConverter converter;

  @PostMapping("/new-training")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Save training")
  public void saveTraining(@RequestParam("traineeUsername") @NotBlank @Size(min = 2, max = 20) String traineeUsername,
                           @RequestParam("trainerUsername") @NotBlank @Size(min = 2, max = 60) String trainerUsername,
                           @RequestParam("trainingName") @NotBlank @Size(min = 2, max = 60) String trainingName,
                           @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam("trainingType") @NotBlank String type,
                           @RequestParam("trainingDuration") @NotBlank @Size(min = 15, max = 60) int duration) {
    TrainingRequest trainingRequest = new TrainingRequest(traineeUsername, trainerUsername, trainingName,
        date, type, duration);
    trainingService.saveTraining(trainingRequest);
  }

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/training-types")
  public List<TrainingTypeResponse> getTrainingTypes() {
    List<TrainingType> types = trainingService.findTrainingTypes();
    return converter.toTrainingTypeResponse(types);
  }
}
