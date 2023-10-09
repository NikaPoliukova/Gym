package org.example.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "trainers")
public class Trainer extends User{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "specialization")
  private String specialization;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;
}
