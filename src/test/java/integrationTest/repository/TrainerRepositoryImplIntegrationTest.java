package integrationTest.repository;

import com.epam.upskill.GymApplication;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dao.TrainingTypeRepository;
import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static integrationTest.repository.TraineeRepositoryImplIntegrationTest.createAndSetTrainee;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GymApplication.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class TrainerRepositoryImplIntegrationTest {
  @Autowired
  TrainerRepository trainerRepository;
  @Autowired
  TraineeRepository traineeRepository;
  @Autowired
  TrainingTypeRepository trainingTypeRepository;
  @Autowired
  TrainingRepository trainingRepository;

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testSaveAndFindById(Trainer trainer) {
    // Act
    Trainer savedTrainer = trainerRepository.save(trainer);
    Optional<Trainer> foundTrainer = trainerRepository.findById(savedTrainer.getId());
    // Assert
    assertAll(
        () -> assertTrue(foundTrainer.isPresent()),
        () -> assertEquals(trainer.getFirstName(), foundTrainer.get().getFirstName()),
        () -> assertEquals(trainer.getLastName(), foundTrainer.get().getLastName()),
        () -> assertEquals(trainer.getUsername(), foundTrainer.get().getUsername())
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testFindByIsActive_WhenTrainersExist(Trainer trainer) {
    // Arrange
    List<Trainer> oldList = trainerRepository.findByIsActive();
    trainingTypeRepository.save(trainer.getSpecialization());
    trainerRepository.save(trainer);
    // Act
    List<Trainer> activeTrainers = trainerRepository.findByIsActive();
    // Assert
    assertAll(
        () -> assertEquals(oldList.size() + 1, activeTrainers.size()),
        () -> assertTrue(activeTrainers.contains(trainer))
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testFindTraineesForTrainer_WhenTraineesExist(Trainer trainer) {
    // Arrange
    TrainingType type = new TrainingType();
    type.setId(9);
    type.setTrainingTypeName(TrainingTypeEnum.YOGA);
    trainingTypeRepository.save(trainer.getSpecialization());
    Trainee trainee = traineeRepository.save(createAndSetTrainee("Ola", "Doe", "ola.doe",
        "address1"));
    Trainee trainee2 = traineeRepository.save(createAndSetTrainee("Oleg", "Doe", "oleg.doe",
        "address5"));
    Trainer savedTrainer = trainerRepository.save(trainer);
    Training training1 = new Training();
    training1.setTrainee(trainee);
    training1.setTrainer(savedTrainer);
    Training training2 = new Training();
    training2.setTrainee(trainee2);
    training2.setTrainer(savedTrainer);
    trainingRepository.save(training1);
    trainingRepository.save(training2);
    // Act
    List<Trainee> trainees = trainerRepository.findTraineesForTrainer(savedTrainer.getId());
    // Assert
    assertAll(
        () -> assertEquals(2, trainees.size()),
        () -> assertTrue(trainees.contains(trainee)),
        () -> assertTrue(trainees.contains(trainee2))
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testFindAll_WhenTrainersExist(Trainer trainer1) {
    // Arrange
    TrainingType type = new TrainingType();
    type.setId(9);
    type.setTrainingTypeName(TrainingTypeEnum.YOGA);
    trainingTypeRepository.save(trainer1.getSpecialization());
    List<Trainer> oldList = trainerRepository.findAll();
    Trainer savedTrainer = trainerRepository.save(trainer1);
    // Act
    List<Trainer> allTrainers = trainerRepository.findAll();
    // Assert
    assertAll(
        () -> assertEquals(oldList.size() + 1, allTrainers.size()),
        () -> assertTrue(allTrainers.contains(savedTrainer))
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testToggleProfileActivation_WhenTrainerFound(Trainer trainer) {
    // Arrange
    trainer.setActive(false);
    Trainer savedTrainer = trainerRepository.save(trainer);
    // Act
    trainerRepository.toggleProfileActivation(trainer);
    // Assert
    Optional<Trainer> toggledTrainer = trainerRepository.findById(savedTrainer.getId());
    assertAll(
        () -> assertTrue(toggledTrainer.isPresent()),
        () -> assertFalse(toggledTrainer.get().isActive())
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testFindByUsername_WhenTrainerFound(Trainer trainer) {
    // Arrange
    TrainingType type = new TrainingType();
    type.setId(9);
    type.setTrainingTypeName(TrainingTypeEnum.YOGA);
    trainingTypeRepository.save(trainer.getSpecialization());
    trainerRepository.save(trainer);
    // Act
    Optional<Trainer> foundTrainer = trainerRepository.findByUsername(trainer.getUsername());
    // Assert
    assertAll(
        () -> assertTrue(foundTrainer.isPresent()),
        () -> assertEquals(trainer.getFirstName(), foundTrainer.get().getFirstName()),
        () -> assertEquals(trainer.getLastName(), foundTrainer.get().getLastName())
    );
  }

  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testFindByUsernameAndPassword_WhenTrainerFound(Trainer trainer) {
    // Arrange
    TrainingType type = new TrainingType();
    type.setId(9);
    type.setTrainingTypeName(TrainingTypeEnum.YOGA);
    trainingTypeRepository.save(trainer.getSpecialization());
    trainer.setPassword("password");
    trainerRepository.save(trainer);
    // Act
    Optional<Trainer> foundTrainer = trainerRepository.findByUsernameAndPassword(trainer.getUsername(), trainer.getPassword());
    // Assert
    assertAll(
        () -> assertTrue(foundTrainer.isPresent()),
        () -> assertEquals(trainer.getFirstName(), foundTrainer.get().getFirstName()),
        () -> assertEquals(trainer.getLastName(), foundTrainer.get().getLastName())
    );
  }


  @ParameterizedTest
  @MethodSource("trainerProvider")
  void testUpdate_WhenTrainerFound(Trainer trainer) {
    // Arrange
    trainerRepository.save(trainer);
    // Act
    trainer.setFirstName("UpdatedFirstName");
    trainer.setLastName("UpdatedLastName");
    trainer.setUsername("updatedUsername");
    trainerRepository.update(trainer);
    // Assert
    Optional<Trainer> foundTrainer = trainerRepository.findById(trainer.getId());
    assertAll(
        () -> assertTrue(foundTrainer.isPresent()),
        () -> assertEquals("UpdatedFirstName", foundTrainer.get().getFirstName()),
        () -> assertEquals("UpdatedLastName", foundTrainer.get().getLastName()),
        () -> assertEquals("updatedUsername", foundTrainer.get().getUsername()));
  }

  private static Stream<Trainer> trainerProvider() {
    TrainingType specialization = createTrainingType("YOGA");
    return Stream.of(
        createAndSetTrainer("John", "Doe", "john.doe", specialization, true)
    );
  }

  static Trainer createAndSetTrainer(String firstName, String lastName, String username,
                                     TrainingType specialization, boolean isActive) {
    Trainer trainer = new Trainer();
    trainer.setFirstName(firstName);
    trainer.setLastName(lastName);
    trainer.setUsername(username);
    trainer.setSpecialization(specialization);
    trainer.setActive(isActive);
    return trainer;
  }


  static TrainingType createTrainingType(String typeName) {
    TrainingType specialization = new TrainingType();
    specialization.setTrainingTypeName(TrainingTypeEnum.valueOf(typeName));
    return specialization;
  }
}
