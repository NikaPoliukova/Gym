package com.epam.upskill.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String username) {
    super("User named '" + username + "' was not found.");
  }
}