package com.epam.upskill.entity;

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

  private String specialization;

  @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  public Trainer(UserBuilder<?, ?> b, String specialization, List<Training> trainings) {
    super(b);
    this.specialization = specialization;
    this.trainings = trainings;
  }
}

