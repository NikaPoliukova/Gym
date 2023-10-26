//package com.epam.upskill.facade;
//
//import com.epam.upskill.entity.User;
//import com.epam.upskill.security.Principal;
//import com.epam.upskill.security.SecurityService;
//import com.epam.upskill.service.TraineeService;
//import com.epam.upskill.service.TrainerService;
//import com.epam.upskill.service.TrainingService;
//import com.epam.upskill.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.BiConsumer;
//
//@RequiredArgsConstructor
//@Service
//public class UserFacade {
//  private final TraineeService traineeService;
//  private final TrainerService trainerService;
//  private final UserService userService;
//  private final SecurityService securityService;
//  private final TrainingService trainingService;
//
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
//        /*case "updatePassword" -> trainerService.updateTrainerPassword(trainerDto);
//        case "update" -> trainerService.updateTrainer(trainerDto);
//        case "toggleActivation" -> userService.toggleProfileActivation(trainer.getId());
//        case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainer.getUsername(), criteria);*/
//        default -> throw new IllegalArgumentException("Operation not found");
//      }
//
//    });
//    strategies.put("Anonimus", (user, operation) -> {
//      switch (operation) {
//        case "printTrainer" -> trainerService.findByUsername(user.getUsername());
//        case "updatePassword" -> trainerService.updateTrainerPassword(trainerDto);
//        case "update" -> trainerService.updateTrainer(trainerDto);
//        case "toggleActivation" -> userService.toggleProfileActivation(trainer.getId());
//        case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainer.getUsername(), criteria);*/
//        default -> throw new IllegalArgumentException("Operation not found");
//      }
//
//    });
//  }
//
//  public void handle(Principal principal, String operation) {
//    var user = securityService.authenticate(principal);
//    strategies.get(user.getRole()).accept(user, operation);
//  }
//
//
//   public void handle(Principal principal, String operation, TraineeDto traineeDto, TrainerDto trainerDto, String criteria) {
//     if (!securityService.authentication(principal)) {
//       throw new IllegalArgumentException("User not found");
//     }
//     var user = userService.findByUsername(principal.username());
//     if (user instanceof Trainee) {
//       handleTraineeOperations((Trainee) user, operation, traineeDto, criteria);
//     } else if (user instanceof Trainer) {
//       handleTrainerOperations((Trainer) user, operation, trainerDto, criteria);
//     }
//   }
//
//  private void handleTraineeOperations(Trainee trainee, String operation, TraineeDto traineeDto, String criteria) {
//    switch (operation) {
//      case "get" -> traineeService.getTraineeByUsername(trainee.getUsername());
//      case "updatePassword" -> traineeService.updateTraineePassword(traineeDto);
//      case "update" -> traineeService.updateTrainee(traineeDto);
//      case "toggleActivation" -> userService.toggleProfileActivation(trainee.getId());
//      case "delete" -> userService.delete(trainee.getId());
//      case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainee.getUsername(), criteria);
//      case "getNotAssignedTrainers" -> trainingService.getNotAssignedActiveTrainersToTrainee(trainee.getId());
//      default -> throw new IllegalArgumentException("Operation not found");
//    }
//  }
//
//  private void handleTrainerOperations(Trainer trainer, String operation, TrainerDto trainerDto, String criteria) {
//    switch (operation) {
//      case "get" -> trainerService.getTrainerByUsername(trainer.getUsername());
//      case "updatePassword" -> trainerService.updateTrainerPassword(trainerDto);
//      case "update" -> trainerService.updateTrainer(trainerDto);
//      case "toggleActivation" -> userService.toggleProfileActivation(trainer.getId());
//      case "getTrainings" -> trainingService.getTrainingsByUsernameAndCriteria(trainer.getUsername(), criteria);
//      default -> throw new IllegalArgumentException("Operation not found");
//    }
//}
//
