package upskill.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import upskill.exception.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(OperationFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleOperationFailedException(OperationFailedException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(IncorrectDateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleUpdateMonthException(IncorrectDateException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(UpdateMonthException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleUpdateMonthException(UpdateMonthException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(UpdateYearException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleUpdateYearException(UpdateYearException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleDateTimeParseException(DateTimeParseException ex) {
    return "Invalid date, enter in the format \"yyyy-mm-dd\"";
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleEntityNotFoundException(EntityNotFoundException ex) {
    return " This trainingType was not found" + ex.getMessage();
  }

  @ExceptionHandler(TrainingNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleTrainingNotFoundException(TrainingNotFoundException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(UpdateDurationException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleUpdateDurationException(UpdateDurationException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleException(ValidationException ex) {
    return new ErrorResponse("Invalid data", ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleException(Exception ex) {
    return new ErrorResponse("Internal Server Error", ex.getMessage());
  }
}
