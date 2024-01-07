package upskill.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "TrainingsSummary")
@TypeAlias("TrainingTrainerSummary")
public class TrainingTrainerSummary {
  @org.springframework.data.annotation.Id
  private String id;
  @Indexed(unique = true)
  @NotBlank(message = "Username cannot be blank")
  private String username;
  @NotBlank(message = "First name cannot be blank")
  private String firstName;
  @NotBlank(message = "Last name cannot be blank")
  private String lastName;
  @NotNull(message = "Status cannot be null")
  private boolean status;
  private List<YearData> yearsList;

  public TrainingTrainerSummary(String username, String firstName, String lastName, boolean status, List<YearData> yearsList) {
    this.username = username;
    this.firstName = firstName;
    this.lastName = lastName;
    this.status = status;
    this.yearsList = yearsList;
  }
}
