package upskill.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.converter.TrainingTypeConverter;
import upskill.dto.TrainingRequest;
import upskill.dto.TrainingRequestDto;
import upskill.dto.TrainingTypeResponse;
import upskill.entity.Training;
import upskill.service.TrainingService;

import javax.validation.Valid;
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
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Save training")
  public Training saveTraining(@RequestBody @Valid TrainingRequest trainingRequest) {
   return trainingService.saveTraining(trainingRequest);
  }

  @PostMapping("/training")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("delete training")
  public void deleteTraining(@RequestBody @Valid TrainingRequestDto trainingRequest) {
    trainingService.delete(trainingRequest);
  }

  @ApiOperation("Get a list of training types")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/training-types")
  public List<TrainingTypeResponse> getTrainingTypes() {
    var types = trainingService.findTrainingTypes();
    return converter.toTrainingTypeResponse(types);
  }
}
