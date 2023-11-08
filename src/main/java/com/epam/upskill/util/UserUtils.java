package com.epam.upskill.util;

import com.epam.upskill.entity.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@UtilityClass
public class UserUtils {

  private static final int LENGTH_OF_PASSWORD = 10;

  public static String generateRandomPassword() {
    return RandomStringUtils.randomAlphanumeric(LENGTH_OF_PASSWORD);
  }

  public static String createUsername(String firstName, String lastName, List<User> users) {
    if (StringUtils.isAnyBlank(firstName, lastName)) {
      throw new IllegalArgumentException("First name or last name must not be null or empty");
    }
    String username = String.format("%s.%s", firstName, lastName);
    return isUsernameUnique(users, username) ? username : username + calculateUsernameCounter(users, username);
  }

  public static boolean isUsernameUnique(List<User> userList, String usernameToCheck) {
    return userList.stream()
        .noneMatch(user -> user.getUsername().equals(usernameToCheck));
  }

  public static int calculateUsernameCounter(List<User> users, String username) {
    return (int) users.stream()
        .map(User::getUsername)
        .filter(userUsername -> userUsername != null && userUsername.startsWith(username))
        .count() + 1;
  }

  public static LocalDate getLocalDate(String dateOfBirth) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateOfBirth, formatter);
  }
}
