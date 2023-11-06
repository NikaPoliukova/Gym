package com.epam.upskill.converter;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.TrainingType;
import com.epam.upskill.service.TraineeService;
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

  @Mapping(source = "specialization.trainingTypeName", target = "specialization")
  TrainerDtoForTrainee toTrainerDtoForTrainee(Trainer trainer);

  List<TrainerDtoForTrainee> toTrainerDtoForTrainee(List<Trainer> listTrainers);

  @Mapping(source = "specialization.trainingTypeName", target = "specialization")
  TrainerResponse toTrainerResponse(Trainer trainer);

  @Mapping(source = "specialization.trainingTypeName", target = "specialization")
  TrainerUpdateResponse toTrainerUpdateResponse(Trainer trainer);

  List<TrainerUpdateResponse> toTrainerUpdateResponse(List<Trainer> trainers);

  Trainer toTrainer(TrainerUpdateRequest request);

}
