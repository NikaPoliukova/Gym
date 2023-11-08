package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.exception.TrainingSaveException;
import com.epam.upskill.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
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
  public void saveTraining(@RequestParam("traineeUsername") @NotBlank String traineeUsername,
                           @RequestParam("trainerUsername") @NotBlank String trainerUsername,
                           @RequestParam("trainingName") @NotBlank String trainingName,
                           @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                           @RequestParam("trainingType") @NotBlank String type,
                           @RequestParam("trainingDuration") @NotBlank int duration) {
    try {
      TrainingRequest trainingRequest = new TrainingRequest(traineeUsername, trainerUsername, trainingName,
          date, type, duration);
      trainingService.saveTraining(trainingRequest);
    } catch (TrainingSaveException ex) {
      throw new TrainingSaveException();
    }
  }

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/training-types")
  public List<TrainingTypeResponse> getTrainingTypes() {
    List<TrainingType> types = trainingService.findTrainingTypes();
    return converter.toTrainingTypeResponse(types);
  }
}
