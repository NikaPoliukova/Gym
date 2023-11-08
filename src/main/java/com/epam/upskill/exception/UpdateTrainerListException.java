package com.epam.upskill.exception;

public class UpdateTrainerListException extends RuntimeException {
  public UpdateTrainerListException(String username) {
    super("Update trainers list for trainee: " + username + " was failed");
  }
}
