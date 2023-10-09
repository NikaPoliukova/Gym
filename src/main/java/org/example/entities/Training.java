package org.example.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "training")
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
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JoinColumn(name = "training_type_id")
  private TrainingType trainingType;

}
