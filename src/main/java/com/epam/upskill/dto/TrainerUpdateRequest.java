package com.epam.upskill.dto;

import java.util.List;

public record TrainerUpdateRequest(
    String username,
    String password,
    String firstName,
    String lastName,
    boolean isActive,
    List<TraineeDtoForTrainer> listTrainees
) {
}
