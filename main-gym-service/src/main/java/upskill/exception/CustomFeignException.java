package upskill.exception;

public class CustomFeignException extends RuntimeException {
  public CustomFeignException(String message, Throwable cause) {
    super(message, cause);
  }

}
