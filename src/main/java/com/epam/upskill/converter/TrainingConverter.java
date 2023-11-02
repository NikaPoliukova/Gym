package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.dto.TrainingRequest;
import com.epam.upskill.dto.TrainingTraineeResponse;
import com.epam.upskill.dto.TrainingTrainerResponse;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface TrainingConverter {
  @Mapping(target = "trainingType", ignore = true)
  @Mapping(target = "trainee", ignore = true)
  @Mapping(target = "trainer", ignore = true)
  Training toTraining(TrainingDto dto, @MappingTarget Training entity);

  @Mapping(target = "trainingType", ignore = true)
  Training toTraining(TrainingRequest trainingRequest, @MappingTarget Training entity);

  @Mapping(source = "trainingName", target = "trainingName")
  @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
  @Mapping(source = "trainingType.trainingTypeName", target = "trainingType")
  @Mapping(source = "trainingDuration", target = "trainingDuration")
  @Mapping(source = "trainer", target = "trainerName", qualifiedByName = "trainerName")
  TrainingTraineeResponse toTrainingResponse(Training training);

  List<TrainingTraineeResponse> toTrainingResponse(List<Training> trainingsList);

  @Mapping(source = "trainingName", target = "trainingName")
  @Mapping(source = "trainingDate", target = "trainingDate", dateFormat = "yyyy-MM-dd")
  @Mapping(source = "trainingDuration", target = "trainingDuration")
  @Mapping(source = "trainee", target = "traineeName", qualifiedByName = "traineeName")
  @Mapping(source = "trainingType.trainingTypeName", target = "trainingType")
  TrainingTrainerResponse toTrainerTrainingResponse(Training training);

  List<TrainingTrainerResponse> toTrainerTrainingResponse(List<Training> trainingsList);


  @Named("traineeName")
  default String mapTraineeName(Trainee trainee) {
    return trainee.getUsername();
  }

  @Named("trainerName")
  default String mapTraineeName(Trainer trainer) {
    return trainer.getUsername();
  }
}
