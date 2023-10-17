package com.epam.upskill.dto;

import java.util.Date;


public record TraineeRegistration(
    String lastName,
    String firstName,
    String address,
    Date dateOfBirth) {
}
