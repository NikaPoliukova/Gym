package com.epam.upskill.converter;

import com.epam.upskill.dto.*;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TrainerConverter {
  Trainer toTrainer(TrainerRegistration trainerRegistration);

  TrainerDtoForTrainee toTrainerDtoForTrainee(Trainer trainer);

  List<TrainerDtoForTrainee> toTrainerDtoForTrainee(List<Trainer> listTrainers);

  TrainerResponse toTrainerResponse(Trainer trainer);
  TrainerUpdateResponse toTrainerUpdateResponse(Trainer trainer);

  List<TrainerUpdateResponse> toTrainerUpdateResponse(List<Trainer> trainers);

  Trainer toTrainer(TrainerUpdateRequest request);
}
