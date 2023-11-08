package com.epam.upskill.dto;

import java.util.List;

public record UpdateTraineeTrainerDto(
    String username,
    String trainingDate,
    String trainingName,
    List<TrainersDtoList> list
) {
}
