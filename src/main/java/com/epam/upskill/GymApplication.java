package com.epam.upskill;


import com.epam.upskill.entity.Training;
import com.epam.upskill.facade.RegistrationFacade;
import com.epam.upskill.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class GymApplication implements CommandLineRunner {


  private final RegistrationFacade registrationFacade;
  private final TrainingService trainingService;


  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    //TrainingDto trainingDto =new TrainingDto("Training 3", LocalDate.now(),
    // 60, 1, 36L, 34L);
    List<Training> trainings = trainingService.getTrainingsByUsernameAndCriteria("Pi.Vova1", "");
    for(var training : trainings){
      log.info("training date = " + training.getTrainingDate() +
          " Длительность= " + training.getTrainingDuration() +
          " TrainingName = " + training.getTrainingName());
    }

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

