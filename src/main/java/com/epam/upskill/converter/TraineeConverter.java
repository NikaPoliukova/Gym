package com.epam.upskill.converter;

import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.entity.Trainee;
import org.mapstruct.Mapper;


@Mapper
public interface TraineeConverter {

  Trainee toTrainee(TraineeRegistration traineeDto);
}
