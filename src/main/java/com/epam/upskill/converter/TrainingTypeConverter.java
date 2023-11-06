package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.dto.TrainingTypeResponse;
import com.epam.upskill.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface TrainingTypeConverter {


  List<TrainingTypeResponse> toTrainingTypeResponse(List<TrainingType> trainingTypes);

  String mapToString(TrainingType trainingType);

  @Mapping(source = "id", target = "trainingTypeId")
  @Mapping(source = "trainingTypeName", target = "trainingType")
  TrainingTypeResponse toTrainingTypeResponse(TrainingType trainingType);

}
