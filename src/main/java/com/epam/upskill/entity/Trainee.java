package com.epam.upskill.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trainee extends User {
  private long id;
  @JsonProperty("date")
  private Date date;
  private String address;


  public Trainee(long userId, String firstName, String lastName, String username, String password, boolean isActive,
                 long id, Date date, String address) {
    super(userId, firstName, lastName, username, password, isActive);
    this.id = id;
    this.date = date;
    this.address = address;
  }
}
