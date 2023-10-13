package com.epam.upskill.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@Data
public class User {
  private long userId;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  @JsonProperty("isActive")
  private boolean isActive;
}
