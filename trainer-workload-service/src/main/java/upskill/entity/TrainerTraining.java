package upskill.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Setter
@Entity
@Table(name = "trainer_training")
public class TrainerTraining {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  long id;

  @Column(name = "trainer_name")
  String trainerUsername;

  @Column(name = "first_name")
  String firstName;

  @Column(name = "last_name")
  String lastName;

  @Column(name = "training_name")
  String trainingName;

  @Column(name = "date")
  LocalDate trainingDate;

  @Column(name = "type")
  String trainingType;

  @Column(name = "duration")
  int trainingDuration;
}
