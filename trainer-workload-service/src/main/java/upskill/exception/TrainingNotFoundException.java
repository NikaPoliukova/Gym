package upskill.exception;

public class TrainingNotFoundException extends RuntimeException {
  public TrainingNotFoundException() {
    super("Training was not found");
  }
}