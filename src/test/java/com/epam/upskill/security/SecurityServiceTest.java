package com.epam.upskill.security;

import com.epam.upskill.entity.User;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SecurityServiceTest {
  @Mock
  private UserService userService;

  @InjectMocks
  private SecurityService securityService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }
  @Test
  void testAuthenticate_WithValidUser() {
        Principal principal = Mockito.mock(Principal.class);
    when(principal.username()).thenReturn("username");
    when(principal.password()).thenReturn("password");
       User user = new User();
    user.setUsername("username");
    user.setPassword("password");
    when(userService.findByUsername("username")).thenReturn(Optional.of(user));
    boolean result = securityService.authenticate(principal);
    assertTrue(result);
  }

  @Test
  void testAuthenticate_WithInvalidUser() {
    Principal principal = Mockito.mock(Principal.class);
    when(principal.username()).thenReturn("username");
    when(principal.password()).thenReturn("password");
    when(userService.findByUsername("username")).thenReturn(Optional.empty());
    assertThrows(IllegalArgumentException.class, () -> securityService.authenticate(principal));
  }
}