package com.epam.upskill.dto;

public record PrepareUserDto(
    long id,
    String username,
    String password,
    String address,
    String specialization,
    String criteria,
    String operation
) {
}
