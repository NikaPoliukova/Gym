//package integrationTest.repository;
//
//import com.epam.upskill.GymApplication;
//import com.epam.upskill.dao.TraineeRepository;
//import com.epam.upskill.dao.TrainerRepository;
//import com.epam.upskill.dao.TrainingRepository;
//import com.epam.upskill.dao.TrainingTypeRepository;
//import com.epam.upskill.dto.TrainingDtoRequest;
//import com.epam.upskill.dto.TrainingTrainerDto;
//import com.epam.upskill.dto.TrainingTypeEnum;
//import com.epam.upskill.entity.Trainee;
//import com.epam.upskill.entity.Trainer;
//import com.epam.upskill.entity.Training;
//import com.epam.upskill.entity.TrainingType;
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
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest(classes = GymApplication.class)
//@TestPropertySource(properties = "spring.config.activate.on-profile=test")
//@EnableTransactionManagement
//@ActiveProfiles("test")
//@Transactional
//class TrainingRepositoryImplIntegrationTest {
//
//  @Autowired
//  private TrainingRepository trainingRepository;
//  @Autowired
//  private TrainingTypeRepository trainingTypeRepository;
//  @Autowired
//  private TrainerRepository trainerRepository;
//  @Autowired
//  private TraineeRepository traineeRepository;
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testSaveAndFindById(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    // Act
//    Training savedTraining = trainingRepository.save(training1);
//    Optional<Training> foundTraining = trainingRepository.findById(savedTraining.getId());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundTraining.isPresent()),
//        () -> assertEquals(training1.getTrainingName(), foundTraining.get().getTrainingName())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testSaveTraining(Training training) {
//    // Act
//    Training savedTraining = trainingRepository.save(training);
//    // Assert
//    assertNotNull(savedTraining);
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindTrainingById(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    training.setTrainer(trainer);
//    training.setTrainee(trainee);
//    training.setTrainingName("Training the best");
//    Training training1 = trainingRepository.save(training);
//    // Act
//    Optional<Training> foundTraining = trainingRepository.findById(training1.getId());
//    // Assert
//    assertAll(
//        () -> assertTrue(foundTraining.isPresent()),
//        () -> assertEquals(training.getId(), foundTraining.get().getId())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindAllTrainings(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    // Act
//    List<Training> oldList = trainingRepository.findAll();
//    Training savedTraining = trainingRepository.save(training1);
//    List<Training> newList = trainingRepository.findAll();
//    // Assert
//    assertAll(
//        () -> assertNotNull(newList),
//        () -> assertTrue(newList.contains(savedTraining)),
//        () -> assertEquals(oldList.size() + 1, newList.size())
//    );
//  }
//
//  @Test
//  void testFindTrainingTypeByName() {
//    // Act
//    TrainingType foundTrainingType = trainingRepository.findTrainingTypeByName("PILATES");
//    // Assert
//    assertAll(
//        () -> assertNotNull(foundTrainingType),
//        () -> assertEquals("PILATES", foundTrainingType.getTrainingTypeName().toString())
//    );
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testGetAssignedActiveTrainersToTrainee(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    trainer.setActive(true);
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    Training savedTraining = trainingRepository.save(training1);
//    // Act
//    List<Trainer> assignedTrainers = trainingRepository.getAssignedActiveTrainersToTrainee(savedTraining.getId());
//    // Assert
//    assertNotNull(assignedTrainers);
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testDeleteTraining(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    trainer.setActive(true);
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    Training savedTraining = trainingRepository.save(training1);
//    // Act
//    trainingRepository.delete(savedTraining);
//    Optional<Training> deletedTraining = trainingRepository.findById(savedTraining.getId());
//    // Assert
//    assertFalse(deletedTraining.isPresent());
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindTrainerTrainings(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    trainee.setUsername("trainee.trainee");
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    trainingRepository.save(training1);
//    // Act
//    List<Training> trainerTrainings = trainingRepository.findTrainerTrainings(
//        new TrainingTrainerDto(trainer.getId(), LocalDate.now().minusMonths(1), LocalDate.now(),
//            trainee.getUsername()));
//    // Assert
//    assertNotNull(trainerTrainings);
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindTrainingTypes() {
//    // Act
//    List<TrainingType> trainingTypes = trainingRepository.findTrainingTypes();
//    // Assert
//    assertNotNull(trainingTypes);
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindTraineeTrainingsList(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    trainee.setUsername("trainee.trainee");
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    trainingRepository.save(training1);
//    // Act
//    List<Training> trainings = trainingRepository.findTraineeTrainingsList(createTrainingDtoRequest(training1.getTrainee().getUsername(),
//        training.getTrainingDate().minusMonths(1), training.getTrainingDate().plusMonths(1),
//        training1.getTrainer().getUsername(), training.getTrainingType().getTrainingTypeName()));
//    // Assert
//    assertNotNull(trainings);
//  }
//
//  @ParameterizedTest
//  @MethodSource("trainingProvider")
//  void testFindTraineeTrainingsListWithArguments(Training training) {
//    // Arrange
//    Trainee trainee = traineeRepository.save(training.getTrainee());
//    Trainer trainer = trainerRepository.save(training.getTrainer());
//    trainee.setUsername("trainee.trainee");
//    Training training1 = new Training();
//    training1.setTrainer(trainer);
//    training1.setTrainee(trainee);
//    training1.setTrainingName("Training the best");
//    training1.setTrainingDate(training.getTrainingDate());
//    trainingRepository.save(training1);
//    // Act
//    List<Training> trainings = trainingRepository.findTraineeTrainingsList(training1.getTrainee().getId(),
//        training.getTrainingDate().toString(), training1.getTrainingName());
//    // Assert
//    assertAll(
//        () -> assertEquals(1, trainings.size()),
//        () -> assertEquals("Training the best", training1.getTrainingName())
//    );
//  }
//
//
//  private static Stream<Training> trainingProvider() {
//    return Stream.of(
//        createAndSetTraining("Training1", LocalDate.now(), 60,
//            createTrainee("trainee.vika"), createTrainer("ola.popova"),
//            TrainerRepositoryImplIntegrationTest.createTrainingType("PILATES"))
//    );
//  }
//
//  static Training createAndSetTraining(String trainingName, LocalDate trainingDate, int trainingDuration,
//                                       Trainee trainee, Trainer trainer, TrainingType trainingType) {
//    Training training = new Training();
//    training.setTrainingName(trainingName);
//    training.setTrainingDate(trainingDate);
//    training.setTrainingDuration(trainingDuration);
//    training.setTrainee(trainee);
//    training.setTrainer(trainer);
//    training.setTrainingType(trainingType);
//    return training;
//  }
//
//  private static TrainingDtoRequest createTrainingDtoRequest(String username, LocalDate periodFrom, LocalDate periodTo,
//                                                             String trainerName, TrainingTypeEnum trainingTypeEnum) {
//    return new TrainingDtoRequest(username, periodFrom, periodTo, trainerName, trainingTypeEnum);
//  }
//
//  static Trainee createTrainee(String username) {
//    Trainee trainee = new Trainee();
//    trainee.setUsername(username);
//    return trainee;
//  }
//
//  static Trainer createTrainer(String username) {
//    Trainer trainer = new Trainer();
//    trainer.setUsername(username);
//    return trainer;
//  }
//
//
//}
