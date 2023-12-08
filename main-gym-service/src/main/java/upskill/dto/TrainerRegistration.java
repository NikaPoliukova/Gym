package upskill.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
@Getter
@Setter
@Validated
public class TrainerRegistration {
  @NotBlank
  @Size(min = 2, max = 30) String firstName;
  @NotBlank @Size(min = 2, max = 30) String lastName;
  @NotBlank String specialization;
}

