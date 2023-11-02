//package com.epam.upskill.facade;
//
//import com.epam.upskill.dto.PrepareUserDto;
//import com.epam.upskill.dto.TraineeDto;
//import com.epam.upskill.dto.TrainerDto;
//import com.epam.upskill.dto.UserDto;
//import com.epam.upskill.entity.Trainee;
//import com.epam.upskill.entity.Trainer;
//import com.epam.upskill.security.Principal;
//import com.epam.upskill.service.TraineeService;
//import com.epam.upskill.service.TrainerService;
//import com.epam.upskill.service.TrainingService;
//import com.epam.upskill.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import static com.epam.upskill.facade.CommandStrategy.*;
//
//@RequiredArgsConstructor
//@Service
//public class UserFacade {
//  private final TraineeService traineeService;
//  private final TrainerService trainerService;
//  private final UserService userService;
//  private final TrainingService trainingService;
//
//
//  public void handle(PrepareUserDto prepareUserDto) {
//    checkIfAuthenticated(prepareUserDto);
//    var user = userService.findByUsername(prepareUserDto.username());
//    if (user instanceof Trainee) {
//      handleTraineeOperations(prepareUserDto);
//    } else if (user instanceof Trainer) {
//      handleTrainerOperations(prepareUserDto);
//    }
//  }
//
//
//  private void handleTraineeOperations(PrepareUserDto userDto) {
//    switch (userDto.operation()) {
//      case GET_COMMAND -> traineeService.findByUsername(userDto.username());
//      case UPDATE_PASSWORD_COMMAND -> userService.updatePassword(new UserDto(userDto.id(), userDto.password()));
//      case UPDATE_COMMAND ->
//          traineeService.updateTrainee(new TraineeDto(userDto.id(), userDto.password(), userDto.address()));
//      case TOGGLE_COMMAND -> traineeService.toggleProfileActivation(userDto.id());
//      case DELETE_COMMAND -> userService.delete(userDto.id());
//      case GET_TRAININGS_COMMAND ->
//          trainingService.findTrainingsByUsernameAndCriteria(userDto.username(), userDto.criteria());
//      case GET_NO_ASSIGNED_TRAININGS_COMMAND -> trainingService.findNotAssignedActiveTrainersToTrainee(userDto.id());
//      default -> throwExceptionIfNotFound();
//    }
//  }
//
//  private void handleTrainerOperations(PrepareUserDto userDto) {
//    switch (userDto.operation()) {
//      case GET_COMMAND -> trainerService.findByUsername(userDto.username());
//      case UPDATE_PASSWORD_COMMAND -> userService.updateUserPassword(new UserDto(userDto.id(), userDto.password()));
//      case UPDATE_COMMAND -> trainerService.updateTrainer(new TrainerDto(userDto.id(), userDto.password(),
//          userDto.address()));
//      case TOGGLE_COMMAND -> trainerService.toggleProfileActivation(userDto.id());
//      case GET_TRAININGS_COMMAND -> trainingService.findTrainingsByUsernameAndCriteria(userDto.username(),
//          userDto.criteria());
//      default -> throwExceptionIfNotFound();
//    }
//  }
//
//  private static void throwExceptionIfNotFound() {
//    throw new IllegalArgumentException("Operation not found");
//  }
//}
//
