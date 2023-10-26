package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper
public interface TrainingConverter {


  TrainingDto toDto(Training training);

  Training toEntity(TrainingDto trainingDto);

}
