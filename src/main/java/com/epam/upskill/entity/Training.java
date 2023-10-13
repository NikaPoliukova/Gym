package com.epam.upskill.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Training {
  private long id;
  private String trainingName;
  private Date trainingDate;
  private int trainingDuration;
  private long trainerId;
  private long traineeId;
  private int trainingTypeId;
}
