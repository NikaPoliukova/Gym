package com.epam.upskill.dto;

import lombok.Getter;

import java.util.Date;


public record TrainingDto(

    String trainingName,
    Date trainingDate,
    int trainingDuration,
    int trainingTypeId) {
  @Override
  public String trainingName() {
    return trainingName;
  }

  @Override
  public Date trainingDate() {
    return trainingDate;
  }

  @Override
  public int trainingDuration() {
    return trainingDuration;
  }

  @Override
  public int trainingTypeId() {
    return trainingTypeId;
  }
}
