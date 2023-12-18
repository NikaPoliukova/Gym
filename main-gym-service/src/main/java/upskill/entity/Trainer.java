package upskill.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Builder(builderMethodName = "trainerBuilder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "trainer_id")
@Table(name = "trainer")
@Entity
public class Trainer extends User {

  @ManyToOne
  @JoinColumn(name = "training_type_id")
  private TrainingType specialization;

  @JsonManagedReference
  @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  @Override
  public String toString() {
    return "Trainer{" +
        "specialization=" + specialization +
        ", trainings=" + this.getTrainings() +
        '}';
  }
}

