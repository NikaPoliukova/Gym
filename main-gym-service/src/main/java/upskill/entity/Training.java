package upskill.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Builder
@Setter
@Entity
@Table(name = "training")
@AllArgsConstructor
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "training_name")
  private String trainingName;

  @Column(name = "training_date")
  private LocalDate trainingDate;

  @Column(name = "training_duration")
  private int trainingDuration;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JsonBackReference
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne
  @JoinColumn(name = "training_type_id")
  private TrainingType trainingType;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Training training = (Training) o;
    return id == training.id; // Сравниваем по id
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public String toString() {
    return "Training(id=" + this.getId() + ", trainingName=" + this.getTrainingName() + ", trainingDate="
        + this.getTrainingDate() + ", trainingDuration=" + this.getTrainingDuration() + ", trainee="
        + this.getTrainee().getId() +
        ", trainer=" + this.getTrainer().getUsername()
        + ", trainingType=" + this.getTrainingType().getId() + ")";
  }
}
