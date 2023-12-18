package upskill.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TrainerTrainingDtoForSave {
  @NotBlank @Size(min = 2, max = 60) String trainerUsername;
  @NotBlank @Size(min = 2, max = 30) String firstName;
  @NotBlank @Size(min = 2, max = 30) String lastName;
  @NotBlank @Size(min = 2, max = 100) String trainingName;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  LocalDate trainingDate;
  @NotBlank String trainingType;
  @NotNull int duration;

}