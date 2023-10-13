package com.epam.upskill.service;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.User;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class UserUtils {
  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  private static final SecureRandom RANDOM = new SecureRandom();

  public static String generateRandomPassword() {
    StringBuilder password = new StringBuilder();
    for (int i = 0; i < 10; i++) {
      int randomIndex = RANDOM.nextInt(CHARACTERS.length());
      char randomChar = CHARACTERS.charAt(randomIndex);
      password.append(randomChar);
    }
    return password.toString();
  }

  public static String createUsername(String firstName, String lastName, Map<Long, Trainee> traineeMap,
                                      Map<Long, Trainer> trainerMap) {
    Map<Long, User> users = new HashMap<>();
    users.putAll(traineeMap);
    users.putAll(trainerMap);
    if (firstName != null && !firstName.isEmpty() && lastName != null && !lastName.isEmpty()) {
      String username = String.format("%s.%s", firstName, lastName);
      if (isUsernameUnique(users, username)) {
        return username;
      } else {
        int count = 0;
        for (User user : users.values()) {
          String userUsername = user.getUsername();
          if (userUsername.startsWith(username)) {
            try {
              int number = Integer.parseInt(userUsername.substring(username.length()));
              if (number >= count) {
                count = number + 1;
              }
            } catch (NumberFormatException e) {
            }
          }
        }
        return username + (count > 0 ? count : "");
      }
    } else {
      throw new IllegalArgumentException("First name and last name must not be null or empty");
    }
  }

  private static boolean isUsernameUnique(Map<Long, User> users, String usernameToCheck) {
    for (User user : users.values()) {
      if (user.getUsername().equals(usernameToCheck)) {
        return false;
      }
    }
    return true;
  }
}
