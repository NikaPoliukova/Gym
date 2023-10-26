package com.epam.upskill.exception;

public class TrainerNotFoundException extends RuntimeException {
  public TrainerNotFoundException(String username) {
    super("Trainer  was not found.");
  }
}
