package com.epam.upskill.dto;

public record TrainingRequest(
    String traineeUsername,
    String trainerUsername,
    String trainingName,
    String trainingDate,
    int trainingDuration
) {
}
