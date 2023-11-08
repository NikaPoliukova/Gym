package com.epam.upskill.exception;

public class UpdateTrainerException extends RuntimeException {
  public UpdateTrainerException(String username) {
    super("Update trainer : " + username + "was failed");
  }
}
