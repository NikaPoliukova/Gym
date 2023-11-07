package com.epam.upskill.dto;


import org.hibernate.validator.constraints.NotBlank;

public record TrainerRegistration(
    @NotBlank
    String firstName,
    @NotBlank
    String lastName,
    @NotBlank
    String specialization
) {
}
