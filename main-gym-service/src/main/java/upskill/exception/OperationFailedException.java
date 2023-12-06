package upskill.exception;

public class OperationFailedException extends RuntimeException {
  public OperationFailedException(String username, String operation) {
    super("Operation " + operation + " was failed " + " for " + username);
  }
}
