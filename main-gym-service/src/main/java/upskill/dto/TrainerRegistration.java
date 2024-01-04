package upskill.dto;


import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Validated
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainerRegistration {
  @NotBlank
  @Size(min = 2, max = 30) String firstName;
  @NotBlank @Size(min = 2, max = 30) String lastName;
  @NotBlank String specialization;
}

