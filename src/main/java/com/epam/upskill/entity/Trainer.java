package com.epam.upskill.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}

