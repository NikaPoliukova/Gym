package com.epam.upskill.controller;

import com.epam.upskill.entity.User;
import com.epam.upskill.exception.AuthenticationException;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


public class LoginControllerTest {

  public static final String USERNAME = "testUser";
  public static final String PASSWORD = "testPassword";
  @InjectMocks
  private LoginController loginController;


  @Mock
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testLoginWithValidCredentials() {
      User user = new User();
    user.setPassword(PASSWORD);
    user.setUsername(USERNAME);
    when(userService.findByUsername(USERNAME)).thenReturn(user);
    loginController.login(USERNAME, PASSWORD);
  }

  @Test
  void testLoginWithInvalidCredentials() {
    User user = new User();
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);
    when(userService.findByUsername(USERNAME)).thenReturn(user);
    loginController.login(USERNAME, PASSWORD);

  }
}
