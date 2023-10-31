package com.epam.upskill.controller;

import com.epam.upskill.converter.TrainerConverter;
import com.epam.upskill.converter.TrainingConverter;
import com.epam.upskill.dto.*;
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
  public ResponseEntity<TrainerResponse> getTrainerProfile(@RequestParam UserDto userDto) {
    var trainer = trainerService.findByUsername(userDto.username());
    var listTrainers = trainerService.findTraineesForTrainer(userDto.id());
    var trainerResponse = converter.toTrainerResponse(trainer);
    //TODO нету листа тренеров
    return ResponseEntity.ok(trainerResponse);
  }

  @PutMapping("/trainer")
  public ResponseEntity<TrainerUpdateResponse> updateTrainerProfile(@RequestBody TrainerUpdateRequest request) {
    var trainer = trainerService.findByUsername(request.username());
    //TODO проверить конвертер
    var trainerUpdateResponse = converter.toTrainerUpdateResponse(trainer);
    return ResponseEntity.ok(trainerUpdateResponse);
  }

  @GetMapping("/trainer/trainings-list")
  public ResponseEntity<List<TrainingTrainerResponse>> getTrainerTrainingsList(@RequestParam String username,
                                                                               @RequestParam String password,
                                                                               @RequestParam(required = false) String periodFrom,
                                                                               @RequestParam(required = false) String periodTo,
                                                                               @RequestParam(required = false) String traineeName
  ) {

    List<Training> trainingsList = trainingService.findTrainingsByUsernameAndCriteria(username, password, periodFrom,
        periodTo, traineeName, "");
    return ResponseEntity.ok(trainingConverter.toTrainerTrainingResponse(trainingsList));

  }

  @PatchMapping("/activate-deactivate-trainer")
  public ResponseEntity<Void> activateDeactivateTrainer(@RequestParam String username, @RequestParam String password,
                                                        @RequestParam boolean isActive) {
    var trainer = trainerService.findByUsernameAndPassword(username, password);
    trainerService.toggleProfileActivation(trainer.getId());
    return ResponseEntity.ok().build();
  }
}
