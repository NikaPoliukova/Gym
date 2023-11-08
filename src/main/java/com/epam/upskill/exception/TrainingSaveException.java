package com.epam.upskill.exception;

public class TrainingSaveException extends RuntimeException {
  public TrainingSaveException() {
    super("Training wasn't save");
  }
}