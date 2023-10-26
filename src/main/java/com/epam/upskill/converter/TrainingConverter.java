package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface TrainingConverter {
  @Mapping(target = "trainingType", ignore = true)
  @Mapping(target = "trainee", ignore = true)
  @Mapping(target = "trainer", ignore = true)
  Training toTraining(TrainingDto dto, @MappingTarget Training entity);

}
