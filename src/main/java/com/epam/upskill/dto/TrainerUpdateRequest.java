package com.epam.upskill.dto;

import java.util.List;

public record TrainerUpdateRequest(
    String username,
    String firstName,
    String lastName,
    boolean isActive
) {
}
