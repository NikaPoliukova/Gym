package com.epam.upskill.dto;

public record TraineeUpdateRequest(
    String username,
    String password,
    String firstName,
    String lastName,
    String dateOfBirth,
    String address,
    boolean isActive
) {
}
