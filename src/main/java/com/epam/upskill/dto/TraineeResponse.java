package com.epam.upskill.dto;

import com.epam.upskill.entity.Trainer;

import java.time.LocalDate;
import java.util.List;

public record TraineeResponse(
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String address,
    boolean isActive,
    List<TrainerDtoForTrainee> trainersList
) {
}
