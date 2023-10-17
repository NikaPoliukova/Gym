package com.epam.upskill.service;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class UserUtils {


  public static String generateRandomPassword() {
    return RandomStringUtils.randomAlphanumeric(10);
  }

  public static String createUsername(String firstName, String lastName, Map<Long, Trainee> traineeMap,
                                      Map<Long, Trainer> trainerMap) {
    if (StringUtils.isAnyBlank(firstName, lastName)) {
      throw new IllegalArgumentException("First name or last name must not be null or empty");
    }

    Map<Long, User> users = prepareUsersMap(traineeMap, trainerMap);
    String username = String.format("%s.%s", firstName, lastName);

    if (!isUsernameUnique(users, username)) {
      username += calculateUsernameCounter(users, username);
    }

    return username;
  }

  public static boolean isUsernameUnique(Map<Long, User> users, String usernameToCheck) {
    return users.values().stream()
        .noneMatch(user -> user.getUsername().equals(usernameToCheck));
  }

  public static Map<Long, User> prepareUsersMap(Map<Long, Trainee> traineeMap, Map<Long, Trainer> trainerMap) {
    return Stream.concat(traineeMap.entrySet().stream(), trainerMap.entrySet().stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }


  public static int calculateUsernameCounter(Map<Long, User> users, String username) {
    int count = 1;
    count += users.values()
        .stream()
        .map(User::getUsername)
        .filter(userUsername -> StringUtils.startsWith(userUsername, username))
        .count();
    return count;
  }
}
