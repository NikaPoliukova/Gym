package com.epam.upskill.dto;

import java.util.List;

public record TrainerUpdateResponse(
    String username,
    String firstName,
    String lastName,
    String specialization,
    boolean isActive,
    List<TraineeDtoForTrainer> trainingList
) {
}
