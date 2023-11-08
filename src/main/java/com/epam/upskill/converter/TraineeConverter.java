package com.epam.upskill.converter;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.service.TraineeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface TraineeConverter {

  Trainee toTrainee(TraineeRegistration traineeDto);

  @Mapping(target = "traineeUsername", expression = "java(mapUserName(trainee))")
  @Mapping(target = "traineeFirstName", expression = "java(mapFirstName(trainee))")
  @Mapping(target = "traineeLastName", expression = "java(mapLastName(trainee))")
  TraineeDtoForTrainer toTraineeDtoForTrainer(Trainee trainee);

  List<TraineeDtoForTrainer> toTraineeDtoForTrainer(List<Trainee> trainees);

  @Mapping(target = "trainersList", expression = "java(trainingsList(trainee,traineeService))")
  @Mapping(target = "isActive", expression = "java(mapIsActive(trainee))")
  TraineeUpdateResponse toTraineeUpdateResponse(Trainee trainee, TraineeService traineeService);

  @Mapping(target = "trainersList", expression = "java(trainingsList(trainee,traineeService))")
  @Mapping(target = "isActive", expression = "java(mapIsActive(trainee))")
  TraineeResponse toTraineeResponse(Trainee trainee, TraineeService traineeService);


  default List<TrainerDtoForTrainee> trainingsList(Trainee trainee, TraineeService traineeService) {
    return traineeService.findTrainersForTrainee(trainee.getId());
  }

  default boolean mapIsActive(Trainee trainee) {
    return trainee.isActive();
  }

  default String mapUserName(Trainee trainee) {
    return trainee.getUsername();
  }

  default String mapFirstName(Trainee trainee) {
    return trainee.getFirstName();
  }

  default String mapLastName(Trainee trainee) {
    return trainee.getLastName();
  }
}
