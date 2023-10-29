package com.epam.upskill.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@RequiredArgsConstructor
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  private String password;

  @Column(name = "is_active")
  private boolean isActive;

  private String role;

//  @Override
//  public String toString() {
//    return "User{" +
//        "id=" + id +
//        ", firstName='" + firstName + '\'' +
//        ", lastName='" + lastName + '\'' +
//        ", username='" + username + '\'' +
//        ", password='" + password + '\'' +
//        ", isActive=" + isActive +
//        ", role='" + role + '\'' +
//        '}';
//  }
}
