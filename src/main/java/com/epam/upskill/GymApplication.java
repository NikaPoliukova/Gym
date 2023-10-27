package com.epam.upskill;


import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.dto.TrainerRegistration;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.facade.RegistrationFacade;
import com.epam.upskill.facade.UserFacade;
import com.epam.upskill.util.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class GymApplication implements CommandLineRunner {

  private final UserFacade userFacade;
  private final RegistrationFacade registrationFacade;

  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {


    /*registrationFacade.registration(new TraineeRegistration("NNN", "DIMA",
        "Minsk", LocalDate.now()));*/
    registrationFacade.registration(new TrainerRegistration("NNN", "DIMA",
        "SPEC3"));
    RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    /*for (int i = 0; i < 2; i++) {
      PrepareUserDto prepareUserDto = randomDataGenerator.generateRandomPrepareUserDtoForTrainer();
      userFacade.handle(prepareUserDto);
      log.info("operation =  " + prepareUserDto.operation() + "  username = " + prepareUserDto.username()
          + "  password = " + prepareUserDto.password() + "  address = " + prepareUserDto.address()
          + "  specialization = " + prepareUserDto.specialization()
          + "  criteria = " + prepareUserDto.criteria()
          + "  id = " + prepareUserDto.id());
    }
    for (int i = 0; i < 2; i++) {
      PrepareUserDto prepareUserDto = randomDataGenerator.generateRandomPrepareUserDtoForTrainee();
      userFacade.handle(prepareUserDto);
      log.info("operation =  " + prepareUserDto.operation() + "  username = " + prepareUserDto.username()
          + "  password = " + prepareUserDto.password() + "  address = " + prepareUserDto.address()
          + "  specialization = " + prepareUserDto.specialization()
          + "  criteria = " + prepareUserDto.criteria()
          + "  id = " + prepareUserDto.id());
    }*/
  }
}



