package upskill.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import upskill.dto.TrainingTypeEnum;

import javax.persistence.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "training_type")
public class TrainingType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Enumerated(EnumType.STRING)
  @Column(name = "training_type_name")
  private TrainingTypeEnum trainingTypeName;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TrainingType that = (TrainingType) o;
    return id == that.id && trainingTypeName == that.trainingTypeName;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (trainingTypeName != null ? trainingTypeName.hashCode() : 0);
    return result;
  }
}
