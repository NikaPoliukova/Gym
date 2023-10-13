package com.epam.upskill.dto;

import java.util.Date;


public record TraineeRegistration(
    String lastName,
    String firstName,
    String address,
    Date dateOfBirth) {
  @Override
  public String lastName() {
    return lastName;
  }

  @Override
  public String firstName() {
    return firstName;
  }

  @Override
  public String address() {
    return address;
  }

  @Override
  public Date dateOfBirth() {
    return dateOfBirth;
  }
}
