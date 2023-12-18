package upskill.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Builder
@Setter
@Entity
@Table(name = "trainer_training")
public class TrainerTraining {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id_")
  private long id;

  @Column(name = "trainer_name")
  private String trainerUsername;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "training_name")
  private String trainingName;

  @Column(name = "date")
  private LocalDate trainingDate;

  @Column(name = "type")
  private String trainingType;

  @Column(name = "duration")
  private int trainingDuration;

  @Override
  public String toString() {
    return "TrainerTraining{" +
        "id=" + id +
        ", trainerUsername='" + trainerUsername + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", trainingName='" + trainingName + '\'' +
        ", trainingDate=" + trainingDate +
        ", trainingType='" + trainingType + '\'' +
        ", trainingDuration=" + trainingDuration +
        '}';
  }
}
