package upskill.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
  private String username;
  private String firstName;
  private String lastName;
  private boolean status;
  private List<YearData> yearsList;
}
