package com.epam.upskill.service;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.util.UserUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserUtilsTest {
  @Test
  public void generateRandomPasswordShouldReturnRandomString() {
    // Arrange
    // Act
    String password1 = UserUtils.generateRandomPassword();
    String password2 = UserUtils.generateRandomPassword();

    // Assert
    assertNotNull(password1);
    assertNotNull(password2);
    assertNotEquals(password1, password2);
    assertEquals(10, password1.length());
    assertEquals(10, password2.length());
  }

  @Test
  public void areGeneratedPasswordsDifferent() {
    // Arrange
    // Act
    String password1 = UserUtils.generateRandomPassword();
    String password2 = UserUtils.generateRandomPassword();

    // Assert
    assertNotEquals(password1, password2);
  }

/*

  @Test
  public void testCalculateUsernameCounter() {
    // Arrange
    User user1 = User.builder().userId(1L).firstName("John").lastName("Doe").username("John.Doe")
        .password("password").isActive(true).build();
    User user2 = User.builder().userId(2L).firstName("Alice").lastName("Smith").username("Alice.Smith")
        .password("password").isActive(true).build();


    Map<Long, User> users = new HashMap<>();
    users.put(user1.getUserId(), user1);
    users.put(user2.getUserId(), user2);

    String username = "John.Doe";

    // Act
    int result = UserUtils.calculateUsernameCounter(users, username);

    // Assert
    assertEquals(3, result);
  }

  @Test
  public void testCreateUsernameWithSameUsername() {
    // Arrange
    Trainee trainee1 = new Trainee(1L, "John", "Doe", "John.Doe", "password",
        true, 1L, new Date(), "123 Main Street");
    Trainer trainer = new Trainer(1L, "John", "Doe", "John.Doe2", "password",
        true, 1L, "fitness");
    Map<Long, Trainee> traineeMap = new HashMap<>();
    traineeMap.put(trainee1.getId(), trainee1);
    Map<Long, Trainer> trainerMap = new HashMap<>();
    trainerMap.put(2L, trainer);
    String firstName = "John";
    String lastName = "Doe";

    // Act
    String username = UserUtils.createUsername(firstName, lastName, traineeMap, trainerMap);

    // Assert
    assertEquals("John.Doe3", username);
  }

  @Test
  public void testCreateUsername() {
    // Arrange
    Map<Long, Trainee> traineeMap = new HashMap<>();
    Map<Long, Trainer> trainerMap = new HashMap<>();

    // Act
    String username = UserUtils.createUsername("John", "Doe", traineeMap, trainerMap);

    // Assert
    assertEquals("John.Doe", username);
  }*/


}