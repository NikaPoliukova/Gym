package com.epam.upskill.handler;

import com.epam.upskill.exception.AuthenticationException;
import com.epam.upskill.exception.RegistrationException;
import com.epam.upskill.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


  @ExceptionHandler(UserNotFoundException.class)
  public String handleUserNotFoundException(UserNotFoundException ex) {
    return "User not found: " + ex.getMessage();
  }

  @ExceptionHandler(AuthenticationException.class)
  public String handleAuthenticationException(AuthenticationException ex) {
    return "User not authenticated " + ex.getMessage();
  }

  @ExceptionHandler(RegistrationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleCustomException(RegistrationException ex) {
    return "Registration was failed " + ex.getMessage();
  }

  @ExceptionHandler(ValidationException.class)
  public ErrorResponse handleException(ValidationException ex) {
    return new ErrorResponse("Invalid data", ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception ex) {
    return new ErrorResponse("Internal Server Error", ex.getMessage());
  }
}
