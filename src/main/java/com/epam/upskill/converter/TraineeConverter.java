package com.epam.upskill.converter;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.service.TraineeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;


@Mapper
public interface TraineeConverter {

  Trainee toTrainee(TraineeRegistration traineeDto);

  TraineeDtoForTrainer toTraineeDtoForTrainer(Trainee trainee);

  List<TraineeDtoForTrainer> toTraineeDtoForTrainer(List<Trainee> trainees);

  @Mapping(target = "trainersList", expression = "java(trainingsList(trainee,traineeService))")
  @Mapping(target = "isActive", expression = "java(mapIsActive(trainee))")
  TraineeUpdateResponse toTraineeUpdateResponse(Trainee trainee, TraineeService traineeService);

  @Mapping(target = "trainersList", expression = "java(trainingsList(trainee,traineeService))")
  TraineeResponse toTraineeResponse(Trainee trainee, TraineeService traineeService);

  @Named("mapTrainersForTrainee")
  default List<TrainerDtoForTrainee> trainingsList(Trainee trainee, TraineeService traineeService) {
    return traineeService.findTrainersForTrainee(trainee.getId());
  }
  @Named("getActive")
  default boolean mapIsActive(Trainee trainee) {
    return trainee.isActive();
  }
}
