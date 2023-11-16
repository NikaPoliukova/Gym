package integrationTest.repository;

import com.epam.upskill.GymApplication;
import com.epam.upskill.dao.TraineeRepository;
import com.epam.upskill.dao.TrainerRepository;
import com.epam.upskill.dao.TrainingRepository;
import com.epam.upskill.dao.TrainingTypeRepository;
import com.epam.upskill.dto.TrainingDtoRequest;
import com.epam.upskill.dto.TrainingTrainerDto;
import com.epam.upskill.dto.TrainingTypeEnum;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.entity.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = GymApplication.class)
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class TrainingRepositoryImplIntegrationTest {

  @Autowired
  private TrainingRepository trainingRepository;
  @Autowired
  private TrainingTypeRepository trainingTypeRepository;
  @Autowired
  private TrainerRepository trainerRepository;
  @Autowired
  private TraineeRepository traineeRepository;

  //
  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testSaveAndFindById(Training training) {
    // Save
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    Training saverdTraining = trainingRepository.save(training1);

    // Find by ID
    Optional<Training> foundTraining = trainingRepository.findById(saverdTraining.getId());

    // Assert
    assertTrue(foundTraining.isPresent());
    assertEquals(training1.getTrainingName(), foundTraining.get().getTrainingName());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testSaveTraining(Training training) {
    Training savedTraining = trainingRepository.save(training);
    assertNotNull(savedTraining);
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindTrainingById(Training training) {

    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    training.setTrainer(trainer);
    training.setTrainee(trainee);
    training.setTrainingName("Training the best");
    Training training1 = trainingRepository.save(training);
    Optional<Training> foundTraining = trainingRepository.findById(training1.getId());

    assertTrue(foundTraining.isPresent());
    assertEquals(training.getId(), foundTraining.get().getId());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindAllTrainings(Training training) {
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    List<Training> oldList = trainingRepository.findAll();
    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    Training savedTraining = trainingRepository.save(training1);
    List<Training> newList = trainingRepository.findAll();
    assertNotNull(newList);
    assertTrue(newList.contains(savedTraining));
    assertEquals(oldList.size() + 1, newList.size());
  }

  @Test
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  void testFindTrainingTypeByName() {
    TrainingType foundTrainingType = trainingRepository.findTrainingTypeByName("PILATES");
    assertNotNull(foundTrainingType);
    assertEquals("PILATES", foundTrainingType.getTrainingTypeName().toString());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testGetAssignedActiveTrainersToTrainee(Training training) {
    // Создаем тестовые данные
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    trainer.setActive(true);

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    Training saverdTraining = trainingRepository.save(training1);

    List<Trainer> assignedTrainers = trainingRepository.getAssignedActiveTrainersToTrainee(saverdTraining.getId());

    assertNotNull(assignedTrainers);
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testDeleteTraining(Training training) {
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    trainer.setActive(true);

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    Training saverdTraining = trainingRepository.save(training1);

    trainingRepository.delete(saverdTraining);

    Optional<Training> deletedTraining = trainingRepository.findById(saverdTraining.getId());
    assertFalse(deletedTraining.isPresent());
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindTrainerTrainings(Training training) {
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    trainee.setUsername("trainee.trainee");

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    trainingRepository.save(training1);
    List<Training> trainerTrainings = trainingRepository.findTrainerTrainings(
        new TrainingTrainerDto(trainer.getId(), LocalDate.now().minusMonths(1), LocalDate.now(),
            trainee.getUsername()));
    assertNotNull(trainerTrainings);
  }

  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindTrainingTypes() {
    List<TrainingType> trainingTypes = trainingRepository.findTrainingTypes();
    assertNotNull(trainingTypes);
  }


  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindTraineeTrainingsList(Training training) {
    // Save
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    trainee.setUsername("trainee.trainee");

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    trainingRepository.save(training1);

    List<Training> trainings = trainingRepository.findTraineeTrainingsList(createTrainingDtoRequest(training1.getTrainee().getUsername(),
        training.getTrainingDate().minusMonths(1), training.getTrainingDate().plusMonths(1),
        training1.getTrainer().getUsername(), training.getTrainingType().getTrainingTypeName()));

    // Assert
    assertNotNull(trainings);
  }


  @ParameterizedTest
  @Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @MethodSource("trainingProvider")
  void testFindTraineeTrainingsListWithArguments(Training training) {
    // Save
    Training training1 = new Training();
    Trainee trainee = traineeRepository.save(training.getTrainee());
    Trainer trainer = trainerRepository.save(training.getTrainer());
    trainee.setUsername("trainee.trainee");

    training1.setTrainer(trainer);
    training1.setTrainee(trainee);
    training1.setTrainingName("Training the best");
    training1.setTrainingDate(training.getTrainingDate());
    trainingRepository.save(training1);

    List<Training> trainings = trainingRepository.findTraineeTrainingsList(training1.getTrainee().getId(),
        training.getTrainingDate().toString(), training1.getTrainingName());

    // Assert
    assertEquals(1, trainings.size());
    assertEquals("Training the best",training1.getTrainingName());
  }


  private static Stream<Training> trainingProvider() {
    return Stream.of(
        createAndSetTraining("Training1", LocalDate.now(), 60,
            createTrainee("trainee.vika"), createTrainer("ola.popova"),
            TrainerRepositoryImplIntegrationTest.createTrainingType("PILATES"))
    );
  }

  static Training createAndSetTraining(String trainingName, LocalDate trainingDate, int trainingDuration,
                                       Trainee trainee, Trainer trainer, TrainingType trainingType) {
    Training training = new Training();
    training.setTrainingName(trainingName);
    training.setTrainingDate(trainingDate);
    training.setTrainingDuration(trainingDuration);
    training.setTrainee(trainee);
    training.setTrainer(trainer);
    training.setTrainingType(trainingType);
    return training;
  }

  private static TrainingDtoRequest createTrainingDtoRequest(String username, LocalDate periodFrom, LocalDate periodTo,
                                                             String trainerName, TrainingTypeEnum trainingTypeEnum) {
    return new TrainingDtoRequest(username, periodFrom, periodTo, trainerName, trainingTypeEnum);
  }

  static Trainee createTrainee(String username) {
    Trainee trainee = new Trainee();
    trainee.setUsername(username);
    return trainee;
  }

  static Trainer createTrainer(String username) {
    Trainer trainer = new Trainer();
    trainer.setUsername(username);
    return trainer;
  }


}
