package com.epam.upskill.exception;

public class UserUpdateException extends RuntimeException {
  public UserUpdateException(String username) {
    super("User wasn't update"
        +username);


  }
}
