package upskill.handler;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import upskill.exception.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleUserNotFoundException(UserNotFoundException ex) {
    return "User not found: " + ex.getMessage();
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public String handleAuthenticationException(AuthenticationException ex) {
    return "User not authenticated " + ex.getMessage();
  }

  @ExceptionHandler(OperationFailedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleOperationFailedException(OperationFailedException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleDateTimeParseException(DateTimeParseException ex) {
    return "Invalid date, enter in the format \"yyyy-mm-dd\"";
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleDateTimeParseException(MethodArgumentTypeMismatchException ex) {
    return "Invalid date, enter in the format \"yyyy-mm-dd\"";
  }

  @ExceptionHandler(ConnectionException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  @ResponseBody
  public String handleConnectionFailedException(ConnectionException ex) {
    return "ex";
  }

  @ExceptionHandler(EntityNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public String handleEntityNotFoundException(EntityNotFoundException ex) {
    return " This trainingType was not found" + ex.getMessage();
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
    return "List can't be empty" + ex.getMessage();
  }

  @ExceptionHandler(RegistrationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleCustomException(RegistrationException ex) {
    return "Registration was failed " + ex.getMessage();
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleException(ValidationException ex) {
    return new ErrorResponse("Invalid data", ex.getMessage());
  }

  @ExceptionHandler(InvalidDataAccessApiUsageException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public String handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
    return "training type was not found";
  }


  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ErrorResponse handleException(Exception ex) {
    return new ErrorResponse("Internal Server Error", ex.getMessage());
  }
}
