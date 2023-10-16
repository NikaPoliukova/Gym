package com.epam.upskill.config;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class Mapper {
  @Autowired
  private ObjectMapper objectMapper;

  public List<Trainee> mapJsonToListTrainee(String jsonFilePath) {
    try {
      File jsonFile = new File(jsonFilePath);
      List<Trainee> trainees = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainee.class));
      log.debug("Mapped JSON to List<Trainee>: " + trainees);
      return trainees;
    } catch (IOException e) {
      log.error("Error mapping JSON to List<Trainee>: " + e.getMessage(), e);
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  public List<Trainer> mapJsonToListTrainer(String jsonFilePath) {
    try {
      File jsonFile = new File(jsonFilePath);
      List<Trainer> trainers = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainer.class));
      log.debug("Mapped JSON to List<Trainer>: " + trainers);
      return trainers;
    } catch (IOException e) {
      log.error("Error mapping JSON to List<Trainer>: " + e.getMessage(), e);
      return Collections.emptyList();
    }
  }

  public List<Training> mapJsonToListTraining(String jsonFilePath) {
    try {
      File jsonFile = new File(jsonFilePath);
      List<Training> trainings = objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Training.class));
      log.debug("Mapped JSON to List<Training>: " + trainings);
      return trainings;
    } catch (IOException e) {
      log.error("Error mapping JSON to List<Training>: " + e.getMessage(), e);
      return Collections.emptyList();
    }
  }
}

