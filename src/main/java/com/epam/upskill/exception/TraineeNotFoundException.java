package com.epam.upskill.exception;

public class TraineeNotFoundException extends RuntimeException {
  public TraineeNotFoundException(String username) {
    super("Trainee  was not found.");
  }
}
