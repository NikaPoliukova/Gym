package com.epam.upskill;


import com.epam.upskill.dto.TrainingDto;
import com.epam.upskill.entity.Trainer;
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

  private final TrainingService trainingService;

  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
     List<Trainer> trainerList =trainingService.getNotAssignedActiveTrainersToTrainee(38L);
     for (Trainer trainer: trainerList){
  log.info("trainerID = " + trainer.getId() + "nameTrainer =" + trainer.getUsername());
     }
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

