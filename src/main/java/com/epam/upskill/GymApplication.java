package com.epam.upskill;


import com.epam.upskill.dto.TraineeRegistration;
import com.epam.upskill.facade.RegistrationFacade;
import com.epam.upskill.facade.UserFacade;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
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
  private final TrainerService trainerService;
  private final TraineeService traineeService;


  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {

    traineeService.createTrainee(
        new TraineeRegistration("Ola", "Gec", "stretching", LocalDate.now()));

    //trainerService.createTrainer(new TrainerRegistration("Nika", "Nika", "SPEC"));

   /* RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    for (int i = 0; i < 10; i++) {
      Principal principal = randomDataGenerator.generatePrincipal();
      String operation = "get";
      String criteria = randomDataGenerator.generateRandomCriteria();
      TraineeDto traineeDto = randomDataGenerator.generateRandomTraineeDto();
      TrainerDto trainerDto = randomDataGenerator.generateRandomTrainerDto();

      userFacade.handle(principal, operation, traineeDto, trainerDto, criteria);
      log.info("запустили метод номер " +i);

    }
    }
  }*/
  }
}

