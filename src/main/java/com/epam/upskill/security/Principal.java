package com.epam.upskill.security;

public record Principal(
    String username,
    String password
) {
}
