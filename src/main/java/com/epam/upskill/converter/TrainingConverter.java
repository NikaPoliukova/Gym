package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTraineeResponse;
import com.epam.upskill.dto.TrainingTrainerResponse;
import com.epam.upskill.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface TrainingConverter {
  @Mapping(target = "trainingType", ignore = true)
  @Mapping(target = "trainee", ignore = true)
  @Mapping(target = "trainer", ignore = true)
  Training toTraining(TrainingDto dto, @MappingTarget Training entity);

  Training toTraining(TrainingRequest trainingRequest, @MappingTarget Training entity);

  TrainingTraineeResponse toTrainingResponse(Training training);

  List<TrainingTraineeResponse> toTrainingResponse(List<Training> trainingsList);

  TrainingTrainerResponse toTrainerTrainingResponse(Training training);

  List<TrainingTrainerResponse> toTrainerTrainingResponse(List<Training> trainingsList);
}
