package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TrainerTrainingDto {
  String trainerUsername;
  String firstName;
  String lastName;
  String trainingName;
  LocalDate trainingDate;
  String trainingType;
  int trainingDuration;

}
