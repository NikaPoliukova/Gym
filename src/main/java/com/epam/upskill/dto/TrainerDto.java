package com.epam.upskill.dto;

public record TrainerDto(
    long id,
    String password,
    String specialization
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
  public String specialization() {
    return specialization;
  }
}
