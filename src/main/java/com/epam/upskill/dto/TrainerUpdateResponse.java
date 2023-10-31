package com.epam.upskill.dto;

public record TrainerUpdateResponse(
    String username,
    String password,
    String firstName,
    String lastName,
    boolean isActive
) {
}
