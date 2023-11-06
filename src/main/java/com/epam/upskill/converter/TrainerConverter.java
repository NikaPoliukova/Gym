package com.epam.upskill.converter;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TrainerService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface TrainerConverter {
  @Mapping(source = "specialization", target = "specialization", qualifiedByName = "mapSpecialization")
  Trainer toTrainer(TrainerRegistration trainerRegistration);


  @Named("mapSpecialization")
  default TrainingType mapSpecialization(String specialization) {
    TrainingType trainingType = new TrainingType();
    trainingType.setTrainingTypeName(TrainingTypeEnum.valueOf(specialization));
    return trainingType;
  }

  @Mapping(target = "specialization", expression = "java(mapEnumToString(trainer.getSpecialization().getTrainingTypeName()))")
  TrainerDtoForTrainee toTrainerDtoForTrainee(Trainer trainer);

  List<TrainerDtoForTrainee> toTrainerDtoForTrainee(List<Trainer> listTrainers);

  @Mapping(target = "specialization", expression = "java(mapEnumToString(trainer.getSpecialization().getTrainingTypeName()))")
  @Mapping(target = "isActive", expression = "java(toBoolean(trainer))")
  @Mapping(target = "traineesList", expression = "java(traineesList(trainer.getId(),trainerService))")
  TrainerResponse toTrainerResponse(Trainer trainer,TrainerService trainerService);

  @Mapping(target = "traineeList", expression = "java(traineesList(trainer.getId(),trainerService))")
  @Mapping(target = "isActive", expression = "java(toBoolean(trainer))")
  @Mapping(target = "specialization", expression = "java(mapEnumToString(trainer.getSpecialization().getTrainingTypeName()))")
  TrainerUpdateResponse toTrainerUpdateResponse(Trainer trainer, TrainerService trainerService);

// List<TrainerUpdateResponse> toTrainerUpdateResponse(List<Trainer> trainers);

//  Trainer toTrainer(TrainerUpdateRequest request);


  default List<TraineeDtoForTrainer> traineesList(long trainerId, TrainerService trainerService) {
    return trainerService.findTraineesForTrainer(trainerId);
  }

  default boolean toBoolean(Trainer trainer) {
    return trainer.isActive();
  }
  default String mapEnumToString(TrainingTypeEnum trainingTypeEnum) {
    return trainingTypeEnum.toString();
  }
}
