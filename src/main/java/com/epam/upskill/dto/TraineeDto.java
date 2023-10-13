package com.epam.upskill.dto;


public record TraineeDto(
    long id,
    String password,
    String address
) {
  @Override
  public long id() {
    return id;
  }

  @Override
  public String password() {
    return password;
  }

  @Override
  public String address() {
    return address;
  }
}

