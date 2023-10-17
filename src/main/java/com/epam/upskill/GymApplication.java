package com.epam.upskill;


import com.epam.upskill.storage.TraineeStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class GymApplication {
  @Autowired
  private final TraineeStorage traineeStorage;

  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }
}
