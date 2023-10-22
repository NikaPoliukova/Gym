package com.epam.upskill.dto;

public record PrepareUserDto(
    String firstName,
    String lastName,
    String username,
    String password
) {
}
