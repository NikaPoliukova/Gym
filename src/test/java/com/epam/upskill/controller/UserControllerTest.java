package com.epam.upskill.controller;

import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.exception.OperationFailedException;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
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
    String password = "password";
    OperationFailedException ex = assertThrows(OperationFailedException.class, () -> {
      userController.changeLogin(USERNAME, password, password);
    });
    assertTrue(ex.getMessage().contains("change login"));
  }

  @Test
  void testChangeLoginWithOperationFailedException() {
    doThrow(new OperationFailedException(USERNAME, "change login")).when(userService)
        .updatePassword(new UserUpdatePass(USERNAME, OLD_PASSWORD, NEW_PASSWORD));
    OperationFailedException ex = assertThrows(OperationFailedException.class, () -> {
      userController.changeLogin(USERNAME, OLD_PASSWORD, NEW_PASSWORD);
    });
    assertEquals("Operation change login for = testUser was failed", ex.getMessage());
  }
}