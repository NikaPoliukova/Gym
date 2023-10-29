package com.epam.upskill.entity;


import com.epam.upskill.dto.TrainingTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

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

  @OneToMany(mappedBy = "trainingType", fetch = FetchType.EAGER)
  private List<Training> trainings;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TrainingType that = (TrainingType) o;

    if (id != that.id) return false;
    if (trainingTypeName != that.trainingTypeName) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id;
    result = 31 * result + (trainingTypeName != null ? trainingTypeName.hashCode() : 0);
    return result;
  }
}
