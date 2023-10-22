package com.epam.upskill.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "trainer")
@Entity
public class Trainer extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "trainer_id")
  private long id;
  @NotBlank
  private String specialization;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "trainer", cascade = CascadeType.REMOVE)
  private List<Training> trainings = new ArrayList<>();
}
