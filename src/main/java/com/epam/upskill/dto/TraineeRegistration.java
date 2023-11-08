package com.epam.upskill.dto;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;

public record TraineeRegistration(
    @NotBlank
    String lastName,
    @NotBlank
    String firstName,
    String address,
    LocalDate dateOfBirth) {
}
