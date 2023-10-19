package com.epam.upskill.dto;

import java.time.LocalDate;
import java.util.Date;


public record TrainingDto(

    String trainingName,
    LocalDate trainingDate,
    int trainingDuration,
    int trainingTypeId) {
}
