package com.epam.upskill.util;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import upskill.entity.User;
import upskill.util.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static upskill.util.UserUtils.createUsername;
import static upskill.util.UserUtils.isUsernameUnique;

class UserUtilsTest {

  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final String PASSWORD = "password";
  public static final String USERNAME = "John.Doe";

  @Test
  void testGenerateRandomPassword() {
    var password = UserUtils.generateRandomPassword();
    assertNotNull(password);
    assertEquals(10, password.length());
  }

  @Test
  void testCreateUsername_Unique() {
    var users = Mockito.mock(List.class);
    Mockito.when(users.stream()).thenReturn(Mockito.mock(Stream.class));
    Mockito.when(users.stream().noneMatch(Mockito.any())).thenReturn(true);
    var username = createUsername(FIRST_NAME, LAST_NAME, users);
    assertNotNull(username);
    assertEquals(USERNAME, username);
  }


  @Test
  void testIsUsernameUnique_Unique() {
    List<User> users = new ArrayList<>();
    var user = User.builder()
        .username(USERNAME)
        .password(PASSWORD)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
    users.add(user);

    boolean isUnique = isUsernameUnique(users, "John.Smith");

    assertTrue(isUnique);
  }

  @Test
  void testIsUsernameUnique_NonUnique() {
    List<User> users = new ArrayList<>();
    var user = User.builder()
        .username(USERNAME)
        .password(PASSWORD)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
    users.add(user);
    boolean isUnique = isUsernameUnique(users, USERNAME);

    assertFalse(isUnique);
  }

  @Test
  void testCalculateUsernameCounter() {
    List<User> users = new ArrayList<>();
    var user1 = User.builder()
        .username(USERNAME)
        .password(PASSWORD)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
    var user2 = User.builder()
        .username(USERNAME)
        .password(PASSWORD)
        .firstName(FIRST_NAME)
        .lastName(LAST_NAME)
        .build();
    users.add(user1);
    users.add(user2);
    int counter = UserUtils.calculateUsernameCounter(users, USERNAME);
    assertEquals(3, counter);
  }
}
