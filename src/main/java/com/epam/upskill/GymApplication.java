package com.epam.upskill;


import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.epam.upskill.facade.RegistrationFacade;
import com.epam.upskill.facade.TrainingFacade;
import com.epam.upskill.facade.UserFacade;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.util.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class GymApplication implements CommandLineRunner {

  private final UserFacade userFacade;
  private final RegistrationFacade registrationFacade;
  private final TrainingFacade trainingFacade;
  private final TraineeService traineeService;
  private final TrainerService trainerService;
  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

   /* trainingFacade.saveTraining(TrainingDto.builder()
        .trainingName("Java")
        .trainingDate(LocalDate.now())
        .trainingDuration(10)
        .trainingTypeId(1)
        .traineeId(38)
        .trainerId(34)
        .build());
    Optional<Trainer> trainer = trainerService.findById(34L);
    log.info("trainer = " + trainer + "  " + trainer.get().getTrainings() + "  " + trainer.get().getSpecialization());
    registrationFacade.registration(new TraineeRegistration("NNN", "DIMA", "Minsk", LocalDate.now()));*/

    RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    for (int i = 0; i < 2; i++) {
      PrepareUserDto prepareUserDto = randomDataGenerator.generateRandomPrepareUserDtoForTrainer();
      userFacade.handle(prepareUserDto);
      log.info("OPERATION FOR TRAINER =  " + prepareUserDto.operation() + "  username = " + prepareUserDto.username()
          + "  password = " + prepareUserDto.password() + "  address = " + prepareUserDto.address()
          + "  specialization = " + prepareUserDto.specialization()
          + "  criteria = " + prepareUserDto.criteria()
          + "  id = " + prepareUserDto.id());
    }
    for (int i = 0; i < 2; i++) {
      PrepareUserDto prepareUserDto = randomDataGenerator.generateRandomPrepareUserDtoForTrainee();
      userFacade.handle(prepareUserDto);
      log.info("OPERATION FOR TRAINEE =  " + prepareUserDto.operation() + "  username = " + prepareUserDto.username()
          + "  password = " + prepareUserDto.password() + "  address = " + prepareUserDto.address()
          + "  specialization = " + prepareUserDto.specialization()
          + "  criteria = " + prepareUserDto.criteria()
          + "  id = " + prepareUserDto.id());
    }
  }
}



