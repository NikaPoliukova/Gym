package upskill.exception;

public class UpdateYearException extends RuntimeException {
  public UpdateYearException() {
    super("failed to update year");
  }
}
