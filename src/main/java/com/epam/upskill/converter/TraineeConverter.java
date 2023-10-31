package com.epam.upskill.converter;

import com.epam.upskill.dto.TraineeDtoForTrainer;
import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TraineeResponse;
import com.epam.upskill.dto.TraineeUpdateResponse;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper
public interface TraineeConverter {
  Trainee toTrainee(TraineeRegistration traineeDto);

  TraineeDtoForTrainer toTraineeDtoForTrainer(Trainee trainee);

  List<TraineeDtoForTrainer> toTraineeDtoForTrainer(List<Trainee> trainees);

  TraineeResponse toTraineeResponse(Trainee trainee);

  TraineeUpdateResponse toTraineeUpdateResponse(Trainee trainee);

}
