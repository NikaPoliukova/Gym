package com.epam.upskill.dto;

import java.util.Date;


public record TrainingDto(

    String trainingName,
    Date trainingDate,
    int trainingDuration,
    int trainingTypeId) {
}
