package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.epam.upskill.util.UserUtils.getLocalDate;

@Api(tags = "Trainings")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainings")
public class TrainingController {
  private final TrainingService trainingService;
  private final TrainingTypeConverter converter;

  @PostMapping("/new-training")
  @ApiOperation("Save training")
  public ResponseEntity<Void> saveTraining(@RequestParam("traineeUsername") @NotBlank String traineeUsername,
                                           @RequestParam("trainerUsername") @NotBlank String trainerUsername,
                                           @RequestParam("trainingName") @NotBlank String trainingName,
                                           @RequestParam("trainingDate") @NotBlank String trainingDate,
                                           @RequestParam("trainingType") @NotBlank String trainingType,
                                           @RequestParam("trainingDuration") @NotBlank int trainingDuration) {
    TrainingRequest trainingRequest = new TrainingRequest(traineeUsername, trainerUsername, trainingName,
        getLocalDate(trainingDate), trainingType, trainingDuration);
    trainingService.saveTraining(trainingRequest);
    return ResponseEntity.ok().build();
  }

  @ApiOperation("Get a list of training types")
  @GetMapping("/get-training-types")
  public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
    List<TrainingType> types = trainingService.findTrainingTypes();
    return ResponseEntity.ok(converter.toTrainingTypeResponse(types));
  }
}
