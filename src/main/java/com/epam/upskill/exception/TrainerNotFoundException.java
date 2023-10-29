package com.epam.upskill.exception;

import java.util.NoSuchElementException;

public class TrainerNotFoundException extends NoSuchElementException {
  public TrainerNotFoundException(String username) {
    super("Trainer  was not found.");
  }
}
