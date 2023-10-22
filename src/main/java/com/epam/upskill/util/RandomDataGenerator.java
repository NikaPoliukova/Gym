package com.epam.upskill.util;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.security.Principal;

import java.util.Random;

public class RandomDataGenerator {
  private Random random = new Random();

  // Массивы с возможными значениями для генерации случайных данных
  private final String[] usernames = {"user1", "user2", "user3"};
  private final String[] passwords = {"password1", "password2", "password3"};
  private final String[] operations = {"get", "updatePassword", "update", "toggleActivation", "delete", "getTrainings",
      "getNotAssignedTrainers"};
  private final String[] criteriaArray = {"criteria_one", "criteria_two", "criteria_three"};

  public Principal generateRandomPrincipal() {
    int randomIndex = random.nextInt(usernames.length);
    String username = usernames[randomIndex];
    String password = passwords[randomIndex];
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
  public TraineeDto generateRandomTraineeDto() {
    int id = random.nextInt(10); // Пример случайной генерации id
    String password = passwords[random.nextInt(passwords.length)];
    String address = "Address" + (id + 1); // Пример случайной генерации адреса
    return new TraineeDto(id, password, address);
  }

  public TrainerDto generateRandomTrainerDto() {
    int id = random.nextInt(10); // Пример случайной генерации id
    String password = passwords[random.nextInt(passwords.length)];
    String specialization = "Specialization" + (id + 1); // Пример случайной генерации специализации
    return new TrainerDto(id, password, specialization);
  }
}
