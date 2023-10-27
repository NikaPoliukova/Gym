package com.epam.upskill.util;

import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.security.Principal;

import java.util.Random;

public class RandomDataGenerator {
  private final Random random = new Random();
  private int currentIndex = 0;
///for Trainer
  private final String[] usernames = {"Pi.Vova1"/*, "Li.Dima1", "Nika.Nika1", "Gec.Ola1", "Gec.Ola2", "Popova.Daria1"*/};
  private final String[] passwords = {"n02GYZWQaK"/*, "8wCIYBzpUa", , "kUZ9P8HPem", "7iZEeidpnG", "8oM8DVuDLf"*/};
  private final long[] ids = {34L};

  ///for Trainee
  private final String[] usernames2 = {"Nika.Nika1"/*, "Gec.Ola1", "Gec.Ola2"*/};
  private final String[] passwords2 = {"nhMlJHUOiF"/*, "8wCIYBzpUa", "nhMlJHUOiF", "kUZ9P8HPem", "7iZEeidpnG", "8oM8DVuDLf"*/};
  private final long[] ids2 = {36L};

  private final String[] operations = {"get", "updatePassword", "update", "toggleActivation",  "getTrainings"};
  private final String[] criteriaArray = {"Training 1", "Training 2", "Training 3"};
  private final String[] specializations = {"SPEC", "Yoga"};


  public PrepareUserDto generateRandomPrepareUserDtoForTrainer() {
    long id = ids[random.nextInt(ids.length)];
    String username = generatePrincipal().username();
    String password = generatePrincipal().password();
    String address = "address" + random.nextInt(1000);
    String specialization = generateSpecialization();
    String criteria = generateRandomCriteria();
    String operation = generateRandomOperation();
    return new PrepareUserDto(id, username, password, address, specialization, criteria, operation);
  }
  public PrepareUserDto generateRandomPrepareUserDtoForTrainee() {
    long id = ids2[random.nextInt(ids.length)];
    String username2 = generatePrincipal().username();
    String password2 = generatePrincipal().password();
    String address = "address" + random.nextInt(1000);
    String specialization = generateSpecialization();
    String criteria = generateRandomCriteria();
    String operation = generateRandomOperation();
    return new PrepareUserDto(id, username2, password2, address, specialization, criteria, operation);
  }

  public String generateSpecialization() {
    int randomIndex = random.nextInt(specializations.length);
    return specializations[randomIndex];
  }

  public Principal generatePrincipal() {
    String username = usernames[currentIndex];
    String password = passwords[currentIndex];
    currentIndex = (currentIndex + 1) % Math.min(usernames.length, passwords.length);
    return new Principal(username, password);
  }

  public String generateRandomOperation() {
    int randomIndex = random.nextInt(operations.length);
    return operations[randomIndex];
  }

  public String generateRandomCriteria() {
    int randomIndex = random.nextInt(criteriaArray.length);
    return criteriaArray[randomIndex];
  }

//  public TraineeDto generateRandomTraineeDto() {
//    int id = random.nextInt(10);
//    String password = passwords[random.nextInt(passwords.length)];
//    String address = "Address" + (id + 1);
//    return new TraineeDto(id, password, address);
//  }
//
//  public TrainerDto generateRandomTrainerDto() {
//    int id = random.nextInt(10);
//    String password = passwords[random.nextInt(passwords.length)];
//    String specialization = "Specialization" + (id + 1);
//    return new TrainerDto(id, password, specialization);
//  }
}
