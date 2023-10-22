package com.epam.upskill.entity;


import com.epam.upskill.dto.TrainingTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


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

  @OneToMany(mappedBy = "trainingType")
  private List<Training> trainings;

}
