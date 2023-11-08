package com.epam.upskill.exception;

public class RegistrationException extends RuntimeException {
  public RegistrationException() {
    super("Registration failed");
  }
}