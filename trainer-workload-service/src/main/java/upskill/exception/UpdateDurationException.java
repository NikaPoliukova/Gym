package upskill.exception;

public class UpdateDurationException extends RuntimeException {
  public UpdateDurationException() {
    super("failed to update duration, it's can't be less than 0");
  }
}
