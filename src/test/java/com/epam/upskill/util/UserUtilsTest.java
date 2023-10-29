package com.epam.upskill.util;

import com.epam.upskill.entity.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.epam.upskill.util.UserUtils.isUsernameUnique;
import static org.junit.jupiter.api.Assertions.*;

class UserUtilsTest {

  @Test
  void testGenerateRandomPassword() {
    String password = UserUtils.generateRandomPassword();
    assertNotNull(password);
    assertEquals(10, password.length());
  }

//  @Test
//  void testCreateUsername_Unique() {
//    String firstName = "John";
//    String lastName = "Doe";
//    List<User> users = new ArrayList<>();
//    when(isUsernameUnique(users, "John.Doe")).thenReturn(true);
//    String username = createUsername(firstName, lastName, users);
//    assertNotNull(username);
//    assertEquals("John.Doe", username);
//  }


  @Test
  void testIsUsernameUnique_Unique() {
    List<User> users = new ArrayList<>();
    User user = User.builder()
        .username("John.Doe")
        .password("password")
        .firstName("John")
        .lastName("Doe")
        .build();
    users.add(user);

    boolean isUnique = isUsernameUnique(users, "John.Smith");

    assertTrue(isUnique);
  }

  @Test
  void testIsUsernameUnique_NonUnique() {
    List<User> users = new ArrayList<>();
    User user = User.builder()
        .username("John.Doe")
        .password("password")
        .firstName("John")
        .lastName("Doe")
        .build();
    users.add(user);
    boolean isUnique = isUsernameUnique(users, "John.Doe");

    assertFalse(isUnique);
  }

  @Test
  void testCalculateUsernameCounter() {
    List<User> users = new ArrayList<>();
    var user1 =User.builder()
        .username("John.Doe")
        .password("password")
        .firstName("John")
        .lastName("Doe")
        .build();
    var user2 =User.builder()
        .username("John.Doe")
        .password("password")
        .firstName("John")
        .lastName("Doe")
        .build();
    users.add(user1);
    users.add(user2);
    int counter = UserUtils.calculateUsernameCounter(users, "John.Doe");
    assertEquals(2, counter);
  }
}
