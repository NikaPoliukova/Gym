package upskill.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@Validated
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistration {
  @NotBlank @Size(min = 2, max = 30) String firstName;
  @NotBlank @Size(min = 2, max = 30) String lastName;
  @Size(min = 2) String address;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  LocalDate dateOfBirth;
}

