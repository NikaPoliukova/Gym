package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Api(tags = "Trainings", description = "Operations for managing trainings")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/trainings")   //Работает
public class TrainingController {
  private final TrainingService trainingService;
  private final TrainingTypeConverter converter;
  private final TraineeService traineeService;
  private final TrainerService trainerService;

  @PostMapping("/add-training")
  public ResponseEntity<Void> saveTraining(@RequestParam String traineeUsername,
                                           @RequestParam String trainerUsername,
                                           @RequestParam String trainingName,
                                           @RequestParam String trainingDate,
                                           @RequestParam String trainingType,
                                           @RequestParam int trainingDuration) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate localDate = LocalDate.parse(trainingDate, formatter);
    TrainingRequest trainingRequest = new TrainingRequest(traineeUsername, trainerUsername, trainingName,
        localDate, trainingType, trainingDuration);
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
