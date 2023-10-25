package com.epam.upskill.security;

import javax.management.relation.Role;

public record Principal(
    String username,
    String password,
    String role
) {
}
