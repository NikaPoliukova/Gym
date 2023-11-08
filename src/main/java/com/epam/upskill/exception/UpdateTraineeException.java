package com.epam.upskill.exception;

public class UpdateTraineeException extends RuntimeException {
  public UpdateTraineeException(String username) {
    super("Update trainee : " + username + "was failed");
  }
}
