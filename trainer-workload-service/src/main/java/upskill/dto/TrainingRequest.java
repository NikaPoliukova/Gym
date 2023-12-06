package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TrainingRequest {
  String trainerUsername;
  String firstName;
  String lastName;
  String trainingName;
  LocalDate trainingDate;
  String trainingType;
  int trainingDuration;
}
