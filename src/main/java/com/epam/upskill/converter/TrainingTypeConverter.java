package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TrainingTypeConverter {


  List<TrainingTypeResponse> toTrainingTypeResponse(List<TrainingType> trainingTypes);

  TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType);
}
