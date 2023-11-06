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

  //работает
  @GetMapping("/trainer")
  public ResponseEntity<TrainerResponse> getTrainerProfile(@RequestParam String username) {
    var trainer = trainerService.findByUsername(username);
    var trainerResponse = converter.toTrainerResponse(trainer, trainerService);
    return ResponseEntity.ok(trainerResponse);
  }

  //работает
  @PutMapping("/trainer")
  public ResponseEntity<TrainerUpdateResponse> updateTrainerProfile(@RequestParam("username") String username,
                                                                    @RequestParam("firstName") String firstName,
                                                                    @RequestParam("lastName") String lastName,
                                                                    @RequestParam("specialization") String specialization,
                                                                    @RequestParam("isActive") boolean isActive) {
    TrainerUpdateRequest request = new TrainerUpdateRequest(username, firstName, lastName, specialization, isActive);
    var trainer = trainerService.update(request);
    var trainerUpdateResponse = converter.toTrainerUpdateResponse(trainer, trainerService);
    return ResponseEntity.ok(trainerUpdateResponse);
  }

  //работает
  @GetMapping("/trainer/trainings-list")
  public ResponseEntity<List<TrainingTrainerResponse>> findTrainerTrainingsList(@RequestParam String username,
                                                                                @RequestParam(required = false) String periodFrom,
                                                                                @RequestParam(required = false) String periodTo,
                                                                                @RequestParam(required = false) String traineeName) {

    List<Training> trainingsList = trainingService.findTrainerTrainings(username, periodFrom,
        periodTo, traineeName);
    return ResponseEntity.ok(trainingConverter.toTrainerTrainingResponse(trainingsList));

  }
  //работает
  @PatchMapping("/activate-deactivate-trainer")
  public ResponseEntity<Void> activateDeactivateTrainer(@RequestParam("username") String username,
                                                        @RequestParam("active") boolean isActive) {
    var trainer = trainerService.findByUsername(username);
    trainerService.toggleProfileActivation(trainer.getId(),isActive);
    return ResponseEntity.ok().build();
  }
}
