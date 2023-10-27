package com.epam.upskill.converter;

import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import org.mapstruct.Mapper;

@Mapper
public interface TrainerConverter {
  Trainer toTrainer(TrainerRegistration trainerRegistration);

}
