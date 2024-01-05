package upskill.exception;

public class IncorrectDateException extends RuntimeException {
  public IncorrectDateException() {
    super("incorrect period");
  }
}