package com.epam.upskill.util;

import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.security.Principal;

import java.util.Random;

public class RandomDataGenerator {
  private final Random random = new Random();
  private int currentIndex = 0;

  private final String[] usernames = {"John.Doe", "Alice.Smith", "Bob.Johnson", "Eve.Wilson", "Michael.Brown",
      "Emma.Davis", "David.Lee", "Sarah.Evans", "Matthew.Lopez", "Olivia.Clark", "William.Hall",
      "Sophia.Turner", "James.White", "Lily.Harris", "Benjamin.Scott", "Christopher.Nelson", "Ava.Thomas",
      "Joseph.Mitchell", "Mia.Perez", "Charles.Sanchez"};

  private final String[] passwords = {"password1", "password2", "password3", "password4", "password5", "password6",
      "password7", "password8", "password9", "password10", "password11", "password12", "password13", "password14",
      "password15", "password16", "password17", "password18", "password19", "password20"};

  private final String[] operations = {"get", "updatePassword", "update", "toggleActivation", "delete", "getTrainings"};
  private final String[] criteriaArray = {"specialization", "address", "trainingName"};

  public Principal generatePrincipal() {
    String username = usernames[currentIndex];
    String password = passwords[currentIndex];
    currentIndex = (currentIndex + 1) % Math.min(usernames.length, passwords.length);
    return new Principal(username, password, "User");
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
    int id = random.nextInt(10);
    String password = passwords[random.nextInt(passwords.length)];
    String address = "Address" + (id + 1);
    return new TraineeDto(id, password, address);
  }

  public TrainerDto generateRandomTrainerDto() {
    int id = random.nextInt(10);
    String password = passwords[random.nextInt(passwords.length)];
    String specialization = "Specialization" + (id + 1);
    return new TrainerDto(id, password, specialization);
  }
}
