package com.epam.upskill.handler;

import com.epam.upskill.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(TraineeNotFoundException.class)
  public ResponseEntity<String> handleTraineeNotFoundException(TraineeNotFoundException ex) {
    return new ResponseEntity<>("Trainee not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TrainerNotFoundException.class)
  public ResponseEntity<String> handleTrainerNotFoundException(TrainerNotFoundException ex) {
    return new ResponseEntity<>("Trainer not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(TrainingNotFoundException.class)
  public ResponseEntity<String> handleTrainingNotFoundException(TrainingNotFoundException ex) {
    return new ResponseEntity<>("Training not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
    return new ResponseEntity<>("User not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
  }

  @ExceptionHandler(RegistrationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleCustomException(RegistrationException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
