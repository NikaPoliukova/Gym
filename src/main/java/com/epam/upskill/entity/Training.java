package com.epam.upskill.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "training")
public class Training {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotBlank
  @Column(name = "training_name")
  private String trainingName;
  @NotBlank
  @Column(name = "training_date")
  private LocalDate trainingDate;
  @NotBlank
  @Column(name = "training_duration")
  private int trainingDuration;

  @ManyToOne
  @JoinColumn(name = "trainee_id")
  private Trainee trainee;

  @ManyToOne
  @JoinColumn(name = "trainer_id")
  private Trainer trainer;

  @ManyToOne
  @JoinColumn(name = "training_type_id")
  private TrainingType trainingType;
}
