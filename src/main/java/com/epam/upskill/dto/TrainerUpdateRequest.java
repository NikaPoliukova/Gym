package com.epam.upskill.dto;

public record TrainerUpdateRequest(
    String username,
    String firstName,
    String lastName,
    String specialization,
    boolean isActive
) {
}
