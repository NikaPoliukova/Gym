package com.epam.upskill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
public class Trainer extends User {
  private long id;
  private String specialization;

  public Trainer(long userId, String firstName, String lastName, String username, String password, boolean isActive,
                 long id, String specialization) {
    super(userId, firstName, lastName, username, password, isActive);
    this.id = id;
    this.specialization = specialization;
  }
}
