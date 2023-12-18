package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingRequestDto {
  @NotBlank @Size(min = 2, max = 60) String trainerUsername;
  @NotBlank @Size(min = 2, max = 100) String trainingName;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  LocalDate trainingDate;
  @NotBlank String trainingType;
  @NotNull int duration;
}
