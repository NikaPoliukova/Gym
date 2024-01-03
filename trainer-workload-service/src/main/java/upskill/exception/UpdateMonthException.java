package upskill.exception;

public class UpdateMonthException extends RuntimeException {
  public UpdateMonthException() {
    super("failed to update month");
  }
}
