package upskill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;

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
  @NotBlank @Size(min = 2, max = 60)
  String trainerUsername;
  @NotBlank @Size(min = 2, max = 30)
  String firstName;
  @NotBlank @Size(min = 2, max = 30)
  String lastName;
  @NotBlank @Size(min = 2, max = 100)
  String trainingName;
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @JsonProperty("trainingDate")
  LocalDate trainingDate;
  @NotBlank String trainingType;
  @NotNull int duration;
}