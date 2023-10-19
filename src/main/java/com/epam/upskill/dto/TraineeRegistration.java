package com.epam.upskill.dto;

import java.time.LocalDate;


public record TraineeRegistration(
    String lastName,
    String firstName,
    String address,
    LocalDate dateOfBirth) {
}
