package com.epam.upskill.entity;

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

  @ManyToOne
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne()
  @JoinColumn(name = "training_type_id")
  private TrainingType trainingType;
}
