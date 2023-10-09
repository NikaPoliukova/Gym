package org.example.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "trainees")
public class Trainee extends User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "trainee_id")
  private long id;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  private String address;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;
}
