package com.epam.upskill.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "trainee")
public class Trainee extends User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  private String address;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "trainee")
  private Set<Training> trainings = new HashSet<>();
}
