package com.epam.upskill.config;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class Mapper {
  private static final Logger logger = LoggerFactory.getLogger(Mapper.class);

  public List<Trainee> mapJsonToListTrainee(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      List<Trainee> trainees = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainee.class));
      logger.debug("Mapped JSON to List<Trainee>: " + trainees);
      return trainees;
    } catch (IOException e) {
      logger.error("Error mapping JSON to List<Trainee>: " + e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public List<Trainer> mapJsonToListTrainer(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      List<Trainer> trainers = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainer.class));
      logger.debug("Mapped JSON to List<Trainer>: " + trainers);
      return trainers;
    } catch (IOException e) {
      logger.error("Error mapping JSON to List<Trainer>: " + e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  public List<Training> mapJsonToListTraining(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      List<Training> trainings = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Training.class));
      logger.debug("Mapped JSON to List<Training>: " + trainings);
      return trainings;
    } catch (IOException e) {
      logger.error("Error mapping JSON to List<Training>: " + e.getMessage(), e);
      return Collections.emptyList();
    }
  }
}

