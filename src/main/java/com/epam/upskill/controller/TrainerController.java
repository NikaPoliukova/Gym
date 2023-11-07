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
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
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
  @ApiOperation("Get Trainer by username")
  public ResponseEntity<TrainerResponse> getTrainer(@RequestParam("username") @NotBlank String username) {
    var trainer = trainerService.findByUsername(username);
    var trainerResponse = converter.toTrainerResponse(trainer, trainerService);
    return ResponseEntity.ok(trainerResponse);
  }

  @PutMapping("/trainer")
  @ApiOperation("Update Trainer's profile")
  public ResponseEntity<TrainerUpdateResponse> updateTrainer(@RequestParam("username") @NotBlank String username,
                                                             @RequestParam("firstName") String firstName,
                                                             @RequestParam("lastName") String lastName,
                                                             @RequestParam("specialization") @NotBlank String specialization,
                                                             @RequestParam("isActive") boolean isActive) {
    TrainerUpdateRequest request = new TrainerUpdateRequest(username, firstName, lastName, specialization, isActive);
    var trainer = trainerService.update(request);
    var trainerUpdateResponse = converter.toTrainerUpdateResponse(trainer, trainerService);
    return ResponseEntity.ok(trainerUpdateResponse);
  }

  @GetMapping("/trainer/trainings")
  @ApiOperation("Find Trainer's trainings")
  public ResponseEntity<List<TrainingTrainerResponse>> findTrainerTrainingsList(
      @RequestParam("username") @NotBlank String username,
      @RequestParam(required = false) String periodFrom,
      @RequestParam(required = false) String periodTo,
      @RequestParam(required = false) String traineeName) {
    List<Training> trainingsList = trainingService.findTrainerTrainings(username, periodFrom,
        periodTo, traineeName);
    return ResponseEntity.ok(trainingConverter.toTrainerTrainingResponse(trainingsList));

  }

  @PatchMapping("/trainer/toggle-activation")
  @ApiOperation("Activate or deactivate Trainer's profile")
  public ResponseEntity<Void> toggleActivation(@RequestParam("username") @NotBlank String username,
                                               @RequestParam("active") @NotBlank boolean isActive) {
    var trainer = trainerService.findByUsername(username);
    trainerService.toggleProfileActivation(trainer.getId(), isActive);
    return ResponseEntity.ok().build();
  }
}
