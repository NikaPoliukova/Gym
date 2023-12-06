//package integrationTest.repository;
//
//
//import com.epam.upskill.GymApplication;
//import com.epam.upskill.dao.UserRepository;
//import com.epam.upskill.entity.User;
//import com.epam.upskill.exception.UserNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest(classes = GymApplication.class)
//@TestPropertySource(properties = "spring.config.activate.on-profile=test")
//@EnableTransactionManagement
//@Transactional
//@ActiveProfiles("test")
//class UserRepositoryIntegrationTest {
//
//  @Autowired
//  private UserRepository userRepository;
//
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testSaveAndFindById(User user) {
//    // Arrange
//    userRepository.save(user);
//    // Act
//    Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundUser.isPresent()),
//        () -> assertEquals(user.getFirstName(), foundUser.get().getFirstName()),
//        () -> assertEquals(user.getLastName(), foundUser.get().getLastName()),
//        () -> assertEquals(user.getUsername(), foundUser.get().getUsername())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testFindAll(User user1) {
//    // Arrange
//    List<User> beforeListUsers = userRepository.findAll();
//    // Act
//    userRepository.save(user1);
//    List<User> newListUsers = userRepository.findAll();
//    // Assert
//    assertAll(
//        () -> assertEquals(beforeListUsers.size() + 1, newListUsers.size()),
//        () -> assertTrue(newListUsers.contains(user1))
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testUpdate(User user) {
//    // Arrange
//    userRepository.save(user);
//    User updatedUser = new User();
//    updatedUser.setFirstName("UpdatedFirstName");
//    updatedUser.setLastName("UpdatedLastName");
//    updatedUser.setUsername("updatedUsername");
//    // Act
//    userRepository.update(updatedUser);
//    Optional<User> foundUser = userRepository.findByUsername(updatedUser.getUsername());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundUser.isPresent()),
//        () -> assertEquals("UpdatedFirstName", foundUser.get().getFirstName()),
//        () -> assertEquals("UpdatedLastName", foundUser.get().getLastName()),
//        () -> assertEquals("updatedUsername", foundUser.get().getUsername())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testDelete(User user) {
//    // Arrange
//    User newUser = userRepository.save(user);
//    // Act
//    userRepository.delete(newUser.getId());
//    Optional<User> deletedUser = userRepository.findById(newUser.getId());
//    // Assert
//    assertFalse(deletedUser.isPresent());
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testFindByUsername(User user) {
//    // Arrange
//    userRepository.save(user);
//    // Act
//    Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundUser.isPresent()),
//        () -> assertEquals(user.getId(), foundUser.get().getId())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testFindByUsernameAndPassword(User user) {
//    // Arrange
//    user.setPassword("1234567890");
//    userRepository.save(user);
//    // Act
//    Optional<User> foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundUser.isPresent()),
//        () -> assertEquals(user.getPassword(), foundUser.get().getPassword())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("userProvider")
//  void testToggleProfileActivation(User user) {
//    // Arrange
//    user.setActive(true);
//    userRepository.save(user);
//    user.setActive(false);
//    // Act
//    userRepository.toggleProfileActivation(user);
//    Optional<User> toggledUser = userRepository.findById(user.getId());
//    // Assert
//    assertAll(
//        () -> assertTrue(toggledUser.isPresent()),
//        () -> assertFalse(toggledUser.get().isActive())
//    );
//  }
//
//  @Test
//  void testFindByUsername_WhenUserNotFound() {
//    // Arrange
//    String nonExistingUsername = "nonexistent";
//    // Act & Assert
//    assertThrows(UserNotFoundException.class, () -> userRepository.findByUsername(nonExistingUsername));
//  }
//
//  @Test
//  void testDelete_WhenUserNotFound() {
//    // Arrange
//    long nonExistingUserId = 555L;
//    // Act & Assert
//    assertThrows(NoSuchElementException.class, () -> userRepository.delete(nonExistingUserId));
//  }
//
//
//  private static Stream<User> userProvider() {
//    return Stream.of(
//        createAndSetUser("John", "Doe", "john.doe"),
//        createAndSetUser("Jane", "Doe", "jane.doe"),
//        createAndSetUser("Bob", "Rol", "bob.rol"),
//        createAndSetUser("Ol", "Smith", "ol.smith"),
//        createAndSetUser("Alice", "Johnson", "Alice.Johnson")
//    );
//  }
//
//  private static User createAndSetUser(String firstName, String lastName, String username) {
//    User user = new User();
//    user.setFirstName(firstName);
//    user.setLastName(lastName);
//    user.setUsername(username);
//    return user;
//  }
//}