package com.epam.upskill.exception;

public class OperationFailedException extends RuntimeException {
  public OperationFailedException(String username, String operation) {
    super("Operation " + operation + " for = " + username + " was failed");
  }
}
