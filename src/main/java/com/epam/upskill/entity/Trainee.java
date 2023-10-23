package com.epam.upskill.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "trainee")
public class Trainee extends User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;
  @NotBlank
  private String address;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  public Trainee(LocalDate dateOfBirth, String address, User user, List<Training> trainings) {
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.user = user;
    this.trainings = trainings;
  }
}
