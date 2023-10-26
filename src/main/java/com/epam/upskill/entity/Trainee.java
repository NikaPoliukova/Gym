package com.epam.upskill.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "trainee_id")
@Entity
@Table(name = "trainee")
public class Trainee extends User {

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  private String address;

  @OneToMany(mappedBy = "trainee", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Training> trainings = new ArrayList<>();

  public Trainee(String firstName, String lastName, String username, String password, LocalDate dateOfBirth,
                 String address, List<Training> trainings) {
    super(firstName, lastName, username, password);
    this.dateOfBirth = dateOfBirth;
    this.address = address;
    this.trainings = trainings;
  }
}
