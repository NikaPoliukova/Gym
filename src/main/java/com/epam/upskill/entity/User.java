package com.epam.upskill.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@SuperBuilder
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank
  @Column(name = "first_name")
  private String firstName;
  @NotBlank
  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  @NotBlank
  private String password;

  @Column(name = "is_active", columnDefinition = "boolean default true")
  private boolean isActive;
}
