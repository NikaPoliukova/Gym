package com.epam.upskill.controller;

import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerTest {

  public static final String USERNAME = "testUser";
  public static final String OLD_PASSWORD = "oldPassword";
  public static final String NEW_PASSWORD = "newPassword";
  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testChangeLoginSuccessful() {
    doNothing().when(userService).updatePassword(new UserUpdatePass(USERNAME, OLD_PASSWORD, NEW_PASSWORD));
    userController.changeLogin(USERNAME, OLD_PASSWORD, NEW_PASSWORD);
    verify(userService).updatePassword(new UserUpdatePass(USERNAME, OLD_PASSWORD, NEW_PASSWORD));
  }

  @Test
  void testChangeLoginWithSamePassword() {
    doThrow(new OperationFailedException("testUser", "change login"))
        .when(userService).updatePassword(any(UserUpdatePass.class));
    assertThrows(OperationFailedException.class, () -> {
      userController.changeLogin("testUser", "oldPassword", "oldPassword");
    });
  }

  @Test
  void testChangeLoginWithOperationFailedException() {
    doThrow(new OperationFailedException("testUser", "change login"))
        .when(userService).updatePassword(any(UserUpdatePass.class));
    assertThrows(OperationFailedException.class, () -> {
      userController.changeLogin("testUser", OLD_PASSWORD, NEW_PASSWORD);
    });
    verify(userService).updatePassword(any(UserUpdatePass.class));
  }
}