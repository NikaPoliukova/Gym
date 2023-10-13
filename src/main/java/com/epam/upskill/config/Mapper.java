package com.epam.upskill.config;

import com.epam.upskill.entity.Trainee;
import com.epam.upskill.entity.Trainer;
import com.epam.upskill.entity.Training;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class Mapper {
  public List<Trainee> mapJsonToListTrainee(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      return objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainee.class));
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
  }

  public List<Trainer> mapJsonToListTrainer(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      return objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Trainer.class));
    } catch (IOException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }

  public List<Training> mapJsonToListTraining(String jsonFilePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      File jsonFile = new File(jsonFilePath);
      return objectMapper.readValue(jsonFile, objectMapper.getTypeFactory()
          .constructCollectionType(List.class, Training.class));
    } catch (IOException e) {
      e.printStackTrace();
      return Collections.emptyList();
    }
  }
}
