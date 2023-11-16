package integrationTest;

import com.epam.upskill.GymApplication;
import com.epam.upskill.dao.UserRepository;
import com.epam.upskill.entity.User;
import com.epam.upskill.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = GymApplication.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@Transactional
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

  @Autowired
  private UserRepository userRepository;


  @ParameterizedTest
  @MethodSource("userProvider")
  void testSaveAndFindById(User user) {
    userRepository.save(user);

    Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

    assertTrue(foundUser.isPresent());
    assertEquals(user.getFirstName(), foundUser.get().getFirstName());
    assertEquals(user.getLastName(), foundUser.get().getLastName());
    assertEquals(user.getUsername(), foundUser.get().getUsername());
  }

  @ParameterizedTest
  @MethodSource("userProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindAll(User user1) {
    List<User> beforeListUsers = userRepository.findAll();
    userRepository.save(user1);

    List<User> newListUsers = userRepository.findAll();
    assertEquals(beforeListUsers.size() + 1, newListUsers.size());
    assertTrue(newListUsers.contains(user1));

  }

  @ParameterizedTest
  @MethodSource("userProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testUpdate(User user) {
    userRepository.save(user);

    User updatedUser = new User();
    updatedUser.setFirstName("UpdatedFirstName");
    updatedUser.setLastName("UpdatedLastName");
    updatedUser.setUsername("updatedUsername");
    System.out.println("new USER = " + updatedUser);
    userRepository.update(updatedUser);

    Optional<User> foundUser = userRepository.findByUsername(updatedUser.getUsername());
    assertTrue(foundUser.isPresent());
    assertEquals("UpdatedFirstName", foundUser.get().getFirstName());
    assertEquals("UpdatedLastName", foundUser.get().getLastName());
    assertEquals("updatedUsername", foundUser.get().getUsername());
  }

  @ParameterizedTest
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  @MethodSource("userProvider")
  void testDelete(User user) {
    User newUser = userRepository.save(user);
    userRepository.delete(newUser.getId());

    Optional<User> deletedUser = userRepository.findById(newUser.getId());
    assertFalse(deletedUser.isPresent());
  }

  @ParameterizedTest
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  @MethodSource("userProvider")
  void testFindByUsername(User user) {
    userRepository.save(user);
    Optional<User> foundUser = userRepository.findByUsername(user.getUsername());

    assertTrue(foundUser.isPresent());
    assertEquals(user.getId(), foundUser.get().getId());
  }

  @ParameterizedTest
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  @MethodSource("userProvider")
  void testFindByUsernameAndPassword(User user) {
    user.setPassword("1234567890");
    userRepository.save(user);
    Optional<User> foundUser = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());

    assertTrue(foundUser.isPresent());
    assertEquals(user.getPassword(), foundUser.get().getPassword());
  }

  @ParameterizedTest
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  @MethodSource("userProvider")
  void testToggleProfileActivation(User user) {
    user.setActive(true);
    userRepository.save(user);

    user.setActive(false);
    userRepository.toggleProfileActivation(user);

    Optional<User> toggledUser = userRepository.findById(user.getId());
    assertTrue(toggledUser.isPresent());
    assertFalse(toggledUser.get().isActive());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindByUsername_WhenUserNotFound() {
    // Arrange
    String nonExistingUsername = "nonexistent";
    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> userRepository.findByUsername(nonExistingUsername));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testDelete_WhenUserNotFound() {
    long nonExistingUserId = 555L;
    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> userRepository.delete(nonExistingUserId));
  }

  private static Stream<User> userProvider() {
    // ¬озвращаем поток тестовых данных User с разными комбинаци€ми заполненных полей
    return Stream.of(
        createAndSetUser("John", "Doe", "john.doe"),
        createAndSetUser("Jane", "Doe", "jane.doe"),
        createAndSetUser("Bob", "Rol", "bob.rol"),
        createAndSetUser("Ol", "Smith", "ol.smith"),
        createAndSetUser("Alice", "Johnson", "Alice.Johnson")
    );
  }

  private static User createAndSetUser(String firstName, String lastName, String username) {
    User user = new User();
    user.setFirstName(firstName);
    user.setLastName(lastName);
    user.setUsername(username);
    return user;
  }
}