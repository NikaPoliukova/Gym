package com.epam.upskill.service;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.User;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class UserUtils {

  public static String generateRandomPassword() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    SecureRandom random = new SecureRandom();

    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int randomIndex = random.nextInt(characters.length());
      char randomChar = characters.charAt(randomIndex);
      password.append(randomChar);
    }
    return password.toString();
  }

  public static String createUsername(String firstName, String lastName, Map<Long, Trainee> traineeMap,
                                      Map<Long, Trainer> trainerMap) {
    if (firstName.isBlank() || lastName.isBlank()) {
      throw new IllegalArgumentException("First name and last name must not be null or empty");
    }

    Map<Long, User> users = prepareUsersMap(traineeMap, trainerMap);
    String username = String.format("%s.%s", firstName, lastName);
    return isUsernameUnique(users, username) ? username : (username + calculateUsernameCounter(users, username));
  }

  public static boolean isUsernameUnique(Map<Long, User> users, String usernameToCheck) {
    return users.values().stream()
        .noneMatch(user -> user.getUsername().equals(usernameToCheck));
  }

  public static Map<Long, User> prepareUsersMap(Map<Long, Trainee> traineeMap, Map<Long, Trainer> trainerMap) {
    Map<Long, User> users = new HashMap<>();
    users.putAll(traineeMap);
    users.putAll(trainerMap);
    return users;
  }

  public static int calculateUsernameCounter(Map<Long, User> users, String username) {
    int count = 1;
    for (User user : users.values()) {
      String userUsername = user.getUsername();
      if (userUsername.startsWith(username)) {
        count++;
      }
    }
    return count;
  }
}
