package com.epam.upskill.dto;

import java.time.LocalDate;

public record TraineeUpdateRequest(
    String username,
    String firstName,
    String lastName,
    LocalDate dateOfBirth,
    String address,
    boolean isActive
) {
}
