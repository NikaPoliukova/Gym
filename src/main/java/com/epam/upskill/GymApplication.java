package com.epam.upskill;


import com.epam.upskill.dto.TraineeDto;
import com.epam.upskill.dto.TrainerDto;
import com.epam.upskill.facade.RegistrationFacade;
import com.epam.upskill.facade.UserFacade;
import com.epam.upskill.security.Principal;
import com.epam.upskill.util.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
    for (int i = 0; i < 10; i++) {
      Principal principal = randomDataGenerator.generatePrincipal();
      String operation = "get";
      String criteria = randomDataGenerator.generateRandomCriteria();
      TraineeDto traineeDto = randomDataGenerator.generateRandomTraineeDto();
      TrainerDto trainerDto = randomDataGenerator.generateRandomTrainerDto();

      userFacade.handle(principal, operation, traineeDto, trainerDto, criteria);
      System.out.println("UserFacade: " + principal.username() + " " + operation + " " + criteria);
    }
  }
}

