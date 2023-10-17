package com.epam.upskill.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Data
public class Trainer extends User {
  private long id;
  private String specialization;
}
