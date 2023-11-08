package com.epam.upskill.dto;

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
