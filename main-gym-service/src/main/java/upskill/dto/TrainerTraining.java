package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TrainerTraining {
  private long id;
  private String trainerUsername;
  private String firstName;
  private String lastName;
  private String trainingName;
  private LocalDate trainingDate;
  private String trainingType;
  private int trainingDuration;
}
