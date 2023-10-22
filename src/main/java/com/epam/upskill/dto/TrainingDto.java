package com.epam.upskill.dto;

import java.time.LocalDate;


public record TrainingDto(

    String trainingName,
    LocalDate trainingDate,
    int trainingDuration,
    int trainingTypeId
) {
}
