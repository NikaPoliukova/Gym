package com.epam.upskill.dto;

public record TrainerRegistration(
    String lastName,
    String firstName,
    String specialization
) {
  @Override
  public String lastName() {
    return lastName;
  }

  @Override
  public String firstName() {
    return firstName;
  }

  @Override
  public String specialization() {
    return specialization;
  }
}
