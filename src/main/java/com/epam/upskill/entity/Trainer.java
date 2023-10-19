package com.epam.upskill.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(name = "trainer")
public class Trainer extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "trainer_id")
  private long id;

  private String specialization;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "trainer")
  private Set<Training> trainings = new HashSet<>();


}
