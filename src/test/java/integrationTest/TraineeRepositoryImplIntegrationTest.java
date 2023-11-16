package integrationTest;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static integrationTest.TrainerRepositoryImplIntegrationTest.createAndSetTrainer;
import static integrationTest.TrainerRepositoryImplIntegrationTest.createTrainingType;
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
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testSaveAndFindById(Trainee trainee) {
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findById(trainee.getId());
    // Assert
    assertTrue(foundTrainee.isPresent());
    assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
    assertEquals(trainee.getLastName(), foundTrainee.get().getLastName());
    assertEquals(trainee.getUsername(), foundTrainee.get().getUsername());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("traineeProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindByUsername_WhenUserFound(Trainee trainee) {
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsername(trainee.getUsername());
    // Assert
    assertTrue(foundTrainee.isPresent());
    assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
    assertEquals(trainee.getLastName(), foundTrainee.get().getLastName());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("traineeProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindByUsernameAndPassword_WhenUserFound(Trainee trainee) {
    trainee.setPassword("password");
    traineeRepository.save(trainee);
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsernameAndPassword(trainee.getUsername(),
        trainee.getPassword());
    // Assert
    assertTrue(foundTrainee.isPresent());
    assertEquals(trainee.getFirstName(), foundTrainee.get().getFirstName());
    assertEquals(trainee.getLastName(), foundTrainee.get().getLastName());
    assertEquals(trainee.getUsername(), foundTrainee.get().getUsername());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("traineeProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testUpdate_WhenUserFound(Trainee trainee) {
    traineeRepository.findAll();
    Trainee savedTrainee = traineeRepository.save(trainee);
    // Act
    trainee.setFirstName("UpdatedFirstName");
    trainee.setLastName("UpdatedLastName");
    trainee.setUsername("updatedUsername");
    traineeRepository.update(trainee);
    // Assert
    Optional<Trainee> foundTrainee = traineeRepository.findById(savedTrainee.getId());
    assertTrue(foundTrainee.isPresent());
    assertEquals("UpdatedFirstName", foundTrainee.get().getFirstName());
    assertEquals("UpdatedLastName", foundTrainee.get().getLastName());
    assertEquals("updatedUsername", foundTrainee.get().getUsername());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("traineeProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindAll_WhenUsersExist(Trainee trainee1) {
    Trainee traineeNew = traineeRepository.save(trainee1);
    List<Trainee> list = traineeRepository.findAll();
    // Assert
    assertEquals(1, list.size());
    assertTrue(list.contains(traineeNew));

  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("traineeProvider")
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testToggleProfileActivation_WhenUserFound(Trainee trainee) {
    trainee.setActive(true);
    Trainee newTrainee = traineeRepository.save(trainee);

    // Act
    trainee.setActive(false);
    traineeRepository.toggleProfileActivation(trainee);

    // Assert
    Optional<Trainee> toggledTrainee = traineeRepository.findById(newTrainee.getId());
    assertTrue(toggledTrainee.isPresent());
    assertFalse(toggledTrainee.get().isActive());
  }

  @ParameterizedTest
  @MethodSource("traineeProvider")
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindTrainersForTrainee_WhenTrainersExist(Trainee trainee) {
    Trainee newTrainee = traineeRepository.save(trainee);
    //  List<Trainee> list = traineeRepository.findAll();
    TrainingType specialization = createTrainingType("PILATES");
    specialization.setId(4);
    Trainer trainer1 = trainerRepository.save(createAndSetTrainer("John", "Doe", "john.doe",
        specialization, true));
    Trainer trainer2 = trainerRepository.save(createAndSetTrainer("Trainer", "Oppov", "john.doe",
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
    assertEquals(2, trainers.size());
    assertTrue(trainers.contains(trainer1));
    assertTrue(trainers.contains(trainer2));
  }


  @Test
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void testFindByUsername_WhenUserNotFound() {
    // Act
    Optional<Trainee> foundTrainee = traineeRepository.findByUsername("nonexistent");

    // Assert
    assertFalse(foundTrainee.isPresent());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void testFindByUsernameAndPassword_WhenUserNotFound() {
    // Act & Assert
    assertThrows(EmptyResultDataAccessException.class, ()
        -> traineeRepository.findByUsernameAndPassword("nonexistent", "password"));
  }

  @Test
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
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
