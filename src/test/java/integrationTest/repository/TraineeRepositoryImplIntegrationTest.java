package integrationTest.repository;

import com.epam.upskill.GymApplication;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GymApplication.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class TraineeRepositoryImplIntegrationTest {

  @Autowired
  private TraineeRepository traineeRepository;
  @Autowired
  TrainerRepository trainerRepository;
  @Autowired
  TrainingRepository trainingRepository;

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testSaveAndFindById(Trainee trainee) {
    // Arrange
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());

    // Assert
    assertAll(
        () -> assertTrue(foundTrainee.isPresent()),
        () -> assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName()),
        () -> assertEquals(trainee.getLastName(), foundTrainee.get().getLastName()),
        () -> assertEquals(trainee.getUsername(), foundTrainee.get().getUsername())
    );
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testFindByUsername_WhenUserFound(Trainee trainee) {
    // Arrange
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsername(trainee.getUsername());
    // Assert
    assertAll(
        () -> assertTrue(foundTrainee.isPresent()),
        () -> assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName()),
        () -> assertEquals(trainee.getLastName(), foundTrainee.get().getLastName())
    );
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testFindByUsernameAndPassword_WhenUserFound(Trainee trainee) {
    // Arrange
    trainee.setPassword("password");
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsernameAndPassword(trainee.getUsername(), trainee.getPassword());
    // Assert
    assertAll(
        () -> assertTrue(foundTrainee.isPresent()),
        () -> assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName()),
        () -> assertEquals(trainee.getLastName(), foundTrainee.get().getLastName()),
        () -> assertEquals(trainee.getUsername(), foundTrainee.get().getUsername())
    );
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testUpdate_WhenUserFound(Trainee trainee) {
    // Arrange
    Trainee savedTrainee = traineeRepository.save(trainee);
    // Act
    trainee.setFirstName("UpdatedFirstName");
    trainee.setLastName("UpdatedLastName");
    trainee.setUsername("updatedUsername");
    traineeRepository.update(trainee);
    // Assert
    Optional<Trainee> foundTrainee = traineeRepository.findById(savedTrainee.getId());
    assertAll(
        () -> assertTrue(foundTrainee.isPresent()),
        () -> assertEquals("UpdatedFirstName", foundTrainee.get().getFirstName()),
        () -> assertEquals("UpdatedLastName", foundTrainee.get().getLastName()),
        () -> assertEquals("updatedUsername", foundTrainee.get().getUsername())
    );
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testFindAll_WhenUsersExist(Trainee trainee1) {
    // Arrange
    Trainee traineeNew = traineeRepository.save(trainee1);
    // Act
    List<Trainee> list = traineeRepository.findAll();

    // Assert
    assertTrue(list.contains(traineeNew));
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testToggleProfileActivation_WhenUserFound(Trainee trainee) {
    // Arrange
    trainee.setActive(true);
    Trainee newTrainee = traineeRepository.save(trainee);
    // Act
    trainee.setActive(false);
    traineeRepository.toggleProfileActivation(trainee);
    // Assert
    Optional<Trainee> toggledTrainee = traineeRepository.findById(newTrainee.getId());
    assertAll(
        () -> assertTrue(toggledTrainee.isPresent()),
        () -> assertFalse(toggledTrainee.get().isActive())
    );
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  void testFindTrainersForTrainee_WhenTrainersExist(Trainee trainee) {
    // Arrange
    Trainee newTrainee = traineeRepository.save(trainee);
    TrainingType specialization = TrainerRepositoryImplIntegrationTest.createTrainingType("PILATES");
    specialization.setId(4);
    Trainer trainer1 = trainerRepository.save(TrainerRepositoryImplIntegrationTest.createAndSetTrainer("John",
        "Doe", "john.doe",
        specialization, true));
    Trainer trainer2 = trainerRepository.save(
        TrainerRepositoryImplIntegrationTest.createAndSetTrainer("Trainer", "Oppov", "john.doe",
            specialization, true));
    Training training1 = new Training();
    training1.setTrainee(newTrainee);
    training1.setTrainer(trainer1);
    Training training2 = new Training();
    training2.setTrainee(newTrainee);
    training2.setTrainer(trainer2);
    trainingRepository.save(training1);
    trainingRepository.save(training2);
    // Act
    List<Trainer> trainers = traineeRepository.findTrainersForTrainee(newTrainee.getId());
    // Assert
    assertAll(
        () -> assertEquals(2, trainers.size()),
        () -> assertTrue(trainers.contains(trainer1)),
        () -> assertTrue(trainers.contains(trainer2))
    );
  }

  @Test
  void testFindByUsername_WhenUserNotFound() {
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsername("nonexistent");
    // Assert
    assertFalse(foundTrainee.isPresent());
  }

  @Test
  void testFindByUsernameAndPassword_WhenUserNotFound() {
    // Act & Assert
    assertThrows(EmptyResultDataAccessException.class,
        () -> traineeRepository.findByUsernameAndPassword("nonexistent", "password"));
  }

  @Test
  void testFindTrainersForTrainee_WhenNoTrainers() {
    // Arrange
    Trainee trainee = traineeRepository.save(createAndSetTrainee("Ola", "Ivanova",
        "Ola.Ivanova", "street"));
    // Act
    List<Trainer> trainers = traineeRepository.findTrainersForTrainee(trainee.getId());
    // Assert
    assertTrue(trainers.isEmpty());
  }

  private static Stream<Trainee> traineeProvider() {
    return Stream.of(
        createAndSetTrainee("John", "Doe", "john.doe", "address1"),
        createAndSetTrainee("Jane", "Doe", "jane.doe", "address2")
    );
  }

  static Trainee createAndSetTrainee(String firstName, String lastName, String username, String address) {
    Trainee trainee = new Trainee();
    trainee.setFirstName(firstName);
    trainee.setLastName(lastName);
    trainee.setUsername(username);
    trainee.setAddress(address);
    return trainee;
  }
}
