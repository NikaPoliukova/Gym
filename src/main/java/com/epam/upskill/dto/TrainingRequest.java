package com.epam.upskill.dto;

import java.time.LocalDate;

public record TrainingRequest(
    String traineeUsername,
    String trainerUsername,
    String trainingName,
    LocalDate trainingDate,
    String trainingType,
    int trainingDuration
) {
}
