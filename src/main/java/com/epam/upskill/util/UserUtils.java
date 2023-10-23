package com.epam.upskill.util;

import com.epam.upskill.entity.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@UtilityClass
public class UserUtils {

  public static String generateRandomPassword() {
    return RandomStringUtils.randomAlphanumeric(10);
  }

  public static String createUsername(String firstName, String lastName, List<User> users) {
    if (StringUtils.isAnyBlank(firstName, lastName)) {
      throw new IllegalArgumentException("First name or last name must not be null or empty");
    }
    String username = String.format("%s.%s", firstName, lastName);

    if (!isUsernameUnique(users, username)) {
      username += calculateUsernameCounter(users, username);
    }
    return username;
  }

  public static boolean isUsernameUnique(List<User> userList, String usernameToCheck) {
    return userList.stream()
        .anyMatch(user -> user.getUsername().equals(usernameToCheck));
  }

  public static int calculateUsernameCounter(List<User> users, String username) {
    return (int) users.stream()
        .map(User::getUsername)
        .filter(userUsername -> userUsername != null && userUsername.startsWith(username))
        .count();
  }


}
