package com.epam.upskill.service.impl;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import upskill.dao.UserRepository;
import upskill.dto.UserDto;
import upskill.dto.UserUpdatePass;
import upskill.entity.User;
import upskill.exception.UserNotFoundException;
import upskill.service.HashPassService;
import upskill.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class UserServiceImplTest {
  public static final long USER_ID = 1L;
  public static final String USERNAME = "testuser";
  public static final String PASSWORD = "password";
  public static final String NEW_PASSWORD = "newPassword";
  @InjectMocks
  private UserServiceImpl userService;

  @Mock
  private UserRepository userRepository;
  @Mock
  private HashPassService hashPassService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testFindById_UserFound() {
    User user = new User();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    User result = userService.findById(USER_ID);
    assertNotNull(result);
    assertSame(user, result);
  }

  @Test
  void testFindById_UserNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));
  }

  @Test
  void testFindAll_UsersFound() {
    List<User> users = new ArrayList<>();
    users.add(new User());
    users.add(new User());
    when(userRepository.findAll()).thenReturn(users);
    List<User> result = userService.findAll();
    assertEquals(users.size(), result.size());
  }

  @Test
  void testFindAll_NoUsersFound() {
    when(userRepository.findAll()).thenReturn(Collections.emptyList());
    List<User> result = userService.findAll();
    assertTrue(result.isEmpty());
  }

  @Test
  void testFindByUsername_UserFound() {
    User user = new User();
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
    User result = userService.findByUsername(USERNAME);
    assertNotNull(result);
    assertSame(user, result);
  }

  @Test
  void testFindByUsername_UserNotFound() {
    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.findByUsername(USERNAME));
  }

  @Test
  void testUpdatePassword() {
    var userUpdatePass = new UserUpdatePass(USERNAME, PASSWORD, NEW_PASSWORD);
    when(hashPassService.hashPass(PASSWORD)).thenReturn("hashedOldPassword");
    when(hashPassService.hashPass("newPassword")).thenReturn("hashedNewPassword");
    when(userRepository.findByUsernameAndPassword(USERNAME, "hashedOldPassword"))
        .thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.updatePassword(userUpdatePass));
  }


  @Test
  void testUpdatePassword_UserNotFound() {
    var updatePass = new UserUpdatePass(USERNAME, PASSWORD, NEW_PASSWORD);
    when(userRepository.findByUsernameAndPassword(updatePass.username(), updatePass.oldPassword()))
        .thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.updatePassword(updatePass));
  }

  @Test
  void testFindByUsername_NonExistingUser() {
    var nonExistingUsername = "nonExistingUser";
    when(userRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.findByUsername(nonExistingUsername));
  }

  @Test
  void testFindByUsernameAndPassword_UserNotFound() {
    when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.findByUsernameAndPassword(USERNAME, PASSWORD));
  }

  @Test
  void testUpdateLogin() {
    var userDto = new UserDto(USER_ID, USERNAME, PASSWORD);
    var user = new User();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    assertDoesNotThrow(() -> userService.updateLogin(userDto));
    assertEquals(USERNAME, user.getUsername());
  }

  @Test
  void testUpdateLogin_UserNotFound() {
    var userDto = new UserDto(USER_ID, USERNAME, PASSWORD);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.updateLogin(userDto));
  }


  @Test
  void testDelete() {
    var user = new User();
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    assertDoesNotThrow(() -> userService.delete(USER_ID));
    verify(userRepository).delete(USER_ID);
  }

  @Test
  void testDelete_UserNotFound() {
    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.delete(USER_ID));
  }
}
