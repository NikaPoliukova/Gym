package com.epam.upskill.exception;

public class TrainingNotFoundException extends RuntimeException {
  public TrainingNotFoundException(String username) {
    super("Training  was not found.");
  }
}
