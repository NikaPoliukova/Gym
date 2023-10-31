package com.epam.upskill.dto;

public record TrainingTrainerResponse(
    String trainingName,
    String trainingDate,
    String trainingType,
    int trainingDuration,
    String traineeName
) {
}
