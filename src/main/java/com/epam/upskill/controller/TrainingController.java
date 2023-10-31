package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainingTypeConverter;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TrainingController {
  private final TrainingService trainingService;
  private final TrainingTypeConverter converter;

  @PostMapping("/add-training")
  public ResponseEntity<Void> addTraining(@RequestBody TrainingRequest request) {
    trainingService.saveTraining(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/get-training-types")
  public ResponseEntity<List<TrainingTypeResponse>> getTrainingTypes() {
    List<TrainingType> types = trainingService.findTrainingTypes();
    return ResponseEntity.ok(converter.toTrainingTypeResponse(types));
  }
}
