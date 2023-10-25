package com.epam.upskill.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;

public record TrainerDto(
    long id,
    String password,
    String specialization
) {
}
