//package com.epam.upskill.service.impl;
//
//import com.epam.upskill.dao.UserRepository;
//import com.epam.upskill.dto.UserDto;
//import com.epam.upskill.dto.UserUpdatePass;
//import com.epam.upskill.entity.User;
//import com.epam.upskill.exception.OperationFailedException;
//import com.epam.upskill.exception.UserNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//class UserServiceImplTest {
//  public static final long USER_ID = 1L;
//  public static final String USERNAME = "testuser";
//  public static final String PASSWORD = "password";
//  @InjectMocks
//  private UserServiceImpl userService;
//
//  @Mock
//  private UserRepository userRepository;
//
//  @BeforeEach
//  void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @Test
//  void testFindById_UserFound() {
//        User user = new User();
//    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
//    User result = userService.findById(USER_ID);
//    assertNotNull(result);
//    assertSame(user, result);
//  }
//
//  @Test
//  void testFindById_UserNotFound() {
//     when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.findById(USER_ID));
//  }
//
//  @Test
//  void testFindAll_UsersFound() {
//    List<User> users = new ArrayList<>();
//    users.add(new User());
//    users.add(new User());
//    when(userRepository.findAll()).thenReturn(users);
//    List<User> result = userService.findAll();
//    assertEquals(users.size(), result.size());
//  }
//
//  @Test
//  void testFindAll_NoUsersFound() {
//    when(userRepository.findAll()).thenReturn(Collections.emptyList());
//    List<User> result = userService.findAll();
//    assertTrue(result.isEmpty());
//  }
//
//  @Test
//  void testFindByUsername_UserFound() {
//    User user = new User();
//    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
//    User result = userService.findByUsername(USERNAME);
//    assertNotNull(result);
//    assertSame(user, result);
//  }
//
//  @Test
//  void testFindByUsername_UserNotFound() {
//    when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.findByUsername(USERNAME));
//  }
//
//  @Test
//  void testUpdatePassword() {
//    UserUpdatePass updatePass = new UserUpdatePass(USERNAME, PASSWORD, "newPassword");
//    User user = new User();
//    when(userRepository.findByUsernameAndPassword(updatePass.username(), updatePass.oldPassword()))
//        .thenReturn(Optional.of(user));
//    assertDoesNotThrow(() -> userService.updatePassword(updatePass));
//  }
//
//  @Test
//  void testUpdatePassword_UserNotFound() {
//    UserUpdatePass updatePass = new UserUpdatePass(USERNAME, PASSWORD, "newPassword");
//    when(userRepository.findByUsernameAndPassword(updatePass.username(), updatePass.oldPassword()))
//        .thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.updatePassword(updatePass));
//  }
//
//  @Test
//  void testFindByUsernameAndPassword_UserFound() {
//    User user = new User();
//    when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.of(user));
//    User result = userService.findByUsernameAndPassword(USERNAME, PASSWORD);
//    assertNotNull(result);
//    assertSame(user, result);
//  }
//
//  @Test
//  void testFindByUsernameAndPassword_UserNotFound() {
//     when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.findByUsernameAndPassword(USERNAME, PASSWORD));
//  }
//
//  @Test
//  void testUpdateLogin() {
//    UserDto userDto = new UserDto(USER_ID, USERNAME,PASSWORD);
//    User user = new User();
//    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
//    assertDoesNotThrow(() -> userService.updateLogin(userDto));
//    assertEquals(USERNAME, user.getUsername());
//  }
//
//  @Test
//  void testUpdateLogin_UserNotFound() {
//    UserDto userDto = new UserDto(USER_ID, USERNAME, PASSWORD);
//    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.updateLogin(userDto));
//  }
//
//
//  @Test
//  void testDelete() {
//      User user = new User();
//    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
//    assertDoesNotThrow(() -> userService.delete(USER_ID));
//    verify(userRepository).delete(USER_ID);
//  }
//
//  @Test
//  void testDelete_UserNotFound() {
//    when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
//    assertThrows(UserNotFoundException.class, () -> userService.delete(USER_ID));
//  }
//}
