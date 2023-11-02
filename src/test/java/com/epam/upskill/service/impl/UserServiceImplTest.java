//package com.epam.upskill.service.impl;
//
//import com.epam.upskill.dao.UserRepository;
//import com.epam.upskill.dto.UserDto;
//import com.epam.upskill.entity.User;
//import com.epam.upskill.exception.UserNotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class UserServiceImplTest {
//  @InjectMocks
//  private UserServiceImpl userService;
//
//  @Mock
//  private UserRepository userRepository;
//
//  @BeforeEach
//  public void setUp() {
//    MockitoAnnotations.openMocks(this);
//  }
//
//  @Test
//  void testFindById() {
//    long userId = 1L;
//    User user = new User();
//    user.setId(userId);
//    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//    Optional<User> result = userService.findById(userId);
//    assertEquals(user, result.orElse(null));
//  }
//
//  @Test
//  void testFindAll() {
//    List<User> users = new ArrayList<>();
//    users.add(new User());
//    users.add(new User());
//
//    when(userRepository.findAll()).thenReturn(users);
//
//    List<User> result = userService.findAll();
//
//    assertEquals(users, result);
//  }
//
//  @Test
//  void testFindByUsername() {
//    String username = "testUser";
//    User user = new User();
//    user.setUsername(username);
//    when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//    Optional<User> result = userService.findByUsername(username);
//    assertEquals(user, result.orElse(null));
//  }
//
//  @Test
//  void testFindByUsernameNotFound() {
//    String username = "nonExistentUser";
//
//    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//    assertThrows(UserNotFoundException.class, () -> userService.findByUsername(username));
//  }
//
//  @Test
//  void testUpdateUserPassword() {
//    long userId = 1L;
//    UserDto userDto = new UserDto(userId, "newPassword");
//    User user = new User();
//    user.setId(userId);
//    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//    userService.updateUserPassword(userDto);
//    verify(userRepository).update(user);
//    assertEquals(userDto.password(), user.getPassword());
//  }
//
//  @Test
//  void testDelete() {
//    long userId = 1L;
//    User user = new User();
//    user.setId(userId);
//
//    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//
//    userService.delete(userId);
//
//    verify(userRepository).delete(userId);
//  }
//
//  @Test
//   void testFindByIdNotFound() {
//    long userId = 1L;
//    when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//    Optional<User> result = userService.findById(userId);
//
//    assertTrue(result.isEmpty());
//  }
//}