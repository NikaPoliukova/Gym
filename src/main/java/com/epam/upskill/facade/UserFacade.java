package com.epam.upskill.facade;

import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.dto.UserDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.security.Principal;
import com.epam.upskill.security.SecurityService;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.TrainingService;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserFacade {
  private final TraineeService traineeService;
  private final TrainerService trainerService;
  private final UserService userService;
  private final SecurityService securityService;
  private final TrainingService trainingService;


  public void handle(PrepareUserDto prepareUserDto) {
    if (!securityService.authenticate(new Principal(prepareUserDto.username(), prepareUserDto.password()))) {
      throw new IllegalArgumentException("User not found");
    }
    var user = userService.findByUsername(prepareUserDto.username());
    if (user.get() instanceof Trainee) {
      handleTraineeOperations(prepareUserDto);
    } else if (user.get() instanceof Trainer) {
      handleTrainerOperations(prepareUserDto);
    }
  }

  private void handleTraineeOperations(PrepareUserDto prepareUserDto) {
    switch (prepareUserDto.operation()) {
      case "get" -> traineeService.findByUsername(prepareUserDto.username());
      case "updatePassword" ->
          userService.updateUserPassword(new UserDto(prepareUserDto.id(), prepareUserDto.password()));
      case "update" -> traineeService.updateTrainee(new TraineeDto(prepareUserDto.id(), prepareUserDto.password(),
          prepareUserDto.address()));
      case "toggleActivation" -> traineeService.toggleProfileActivation(prepareUserDto.id());
      case "delete" -> userService.delete(prepareUserDto.id());
      case "getTrainings" -> trainingService.findTrainingsByUsernameAndCriteria(prepareUserDto.username(),
          prepareUserDto.criteria());
      case "getNotAssignedTrainers" -> trainingService.findNotAssignedActiveTrainersToTrainee(prepareUserDto.id());
      default -> throw new IllegalArgumentException("Operation not found");
    }
  }

  private void handleTrainerOperations(PrepareUserDto prepareUserDto) {
    switch (prepareUserDto.operation()) {
      case "get" -> trainerService.findByUsername(prepareUserDto.username());
      case "updatePassword" ->
          userService.updateUserPassword(new UserDto(prepareUserDto.id(), prepareUserDto.password()));
      case "update" -> trainerService.updateTrainer(new TrainerDto(prepareUserDto.id(), prepareUserDto.password(),
          prepareUserDto.address()));
      case "toggleActivation" -> trainerService.toggleProfileActivation(prepareUserDto.id());
      case "getTrainings" -> trainingService.findTrainingsByUsernameAndCriteria(prepareUserDto.username(),
          prepareUserDto.criteria());
      default -> throw new IllegalArgumentException("Operation not found");
    }
  }
//  private final Map<String, BiConsumer<User, String>> strategies = new HashMap<>();
//
//  @PostConstruct
//  private void init() {
//    strategies.put("Admin", (user, operation) -> {
//      switch (operation) {
//        case "printTrainer" -> trainerService.findByUsername(user.getUsername());
//        /*case "updatePassword" -> trainerService.updateTrainerPassword(trainerDto);
//        case "update" -> trainerService.updateTrainer(trainerDto);
//        case "toggleActivation" -> userService.toggleProfileActivation(trainer.getId());
//        case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainer.getUsername(), criteria);*/
//        default -> throw new IllegalArgumentException("Operation not found");
//      }
//
//    });
//    strategies.put("User", (user, operation) -> {
//      switch (operation) {
//        case "printTrainer" -> trainerService.findByUsername(user.getUsername());
//        case "getTrainings" -> trainingService.findTrainingsByUsernameAndCriteria();
//        case "NotActiveTrainersForTrainee" -> trainingService.findNotAssignedActiveTrainersToTrainee();
//        case "findActiveTrainers" -> trainerService.findByUsername();
//        /*case "updatePassword" -> trainerService.updateTrainerPassword(trainerDto);
//        case "update" -> trainerService.updateTrainer(trainerDto);
//        case "toggleActivation" -> userService.toggleProfileActivation(trainer.getId());
//        case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainer.getUsername(), criteria);*/
//        default -> throw new IllegalArgumentException("Operation not found");
//      }
//
//    });
//    strategies.put("Anonymous", (user, operation) -> {
//      switch (operation) {
//        case "registrationTrainee" -> traineeService.saveTrainee();
//        case "registrationTrainer" -> trainerService.saveTrainer();
//        default -> throw new IllegalArgumentException("Operation not found");
//      }
//    });
//  }
//  private final Map<String, BiConsumer<User, String>> strategies = new HashMap<>();
//
//  public void handle(Principal principal, String operation) {
//    var user = securityService.authenticate(principal);
//    strategies.get(user.getRole()).accept(user, operation);
//  }


}

