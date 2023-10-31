package com.epam.upskill.dto;

import java.util.List;

public record TraineeTrainerListRequest(
    String traineeUsername,
    List<TrainerDtoForTrainee> trainersList
) {
}
