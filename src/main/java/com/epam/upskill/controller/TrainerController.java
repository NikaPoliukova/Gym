package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.TrainerResponse;
import com.epam.upskill.dto.TrainerUpdateRequest;
import com.epam.upskill.dto.TrainerUpdateResponse;
import com.epam.upskill.dto.TrainingTrainerResponse;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/trainers")
public class TrainerController {

  private final TrainerService trainerService;
  private final TrainerConverter converter;
  private final TrainingService trainingService;
  private final TrainingConverter trainingConverter;

  @GetMapping("/trainer")
  public ResponseEntity<TrainerResponse> getTrainerProfile(@RequestParam String username) {
    var trainer = trainerService.findByUsername(username);
    var listTrainers = trainerService.findTraineesForTrainer(trainer.getId());
    var trainerResponse = converter.toTrainerResponse(trainer);
    //TODO нету листа тренеров
    return ResponseEntity.ok(trainerResponse);
  }

  @PutMapping("/trainer")
  public ResponseEntity<TrainerUpdateResponse> updateTrainerProfile(@RequestParam String username,
                                                                    @RequestParam String firstName,
                                                                    @RequestParam String lastName,
                                                                    @RequestParam("isActive") boolean isActive) {
    TrainerUpdateRequest request = new TrainerUpdateRequest(username, firstName, lastName, isActive);
    var trainer = trainerService.updateTrainer(request);
    //TODO проверить конвертер
    //TODO нету листа тренеров
    var trainerUpdateResponse = converter.toTrainerUpdateResponse(trainer);
    return ResponseEntity.ok(trainerUpdateResponse);
  }

  @GetMapping("/trainer/trainings-list")
  public ResponseEntity<List<TrainingTrainerResponse>> getTrainerTrainingsList(@RequestParam String username,
                                                                               @RequestParam(required = false) String periodFrom,
                                                                               @RequestParam(required = false) String periodTo,
                                                                               @RequestParam(required = false) String traineeName
  ) {

    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(username, periodFrom,
        periodTo, traineeName, "");
    return ResponseEntity.ok(trainingConverter.toTrainerTrainingResponse(trainingsList));

  }

  @PatchMapping("/activate-deactivate-trainer")
  public ResponseEntity<Void> activateDeactivateTrainer(@RequestParam String username,
                                                        @RequestParam boolean isActive) {
    var trainer = trainerService.findByUsername(username);
    trainerService.toggleProfileActivation(trainer.getId());
    return ResponseEntity.ok().build();
  }
}
