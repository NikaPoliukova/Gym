package com.epam.upskill.facade;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class CommandStrategy {
  public static final String GET_COMMAND = "get";
  public static final String UPDATE_PASSWORD_COMMAND = "updatePassword";
  public static final String UPDATE_COMMAND = "update";
  public static final String TOGGLE_COMMAND = "toggleActivation";
  public static final String GET_TRAININGS_COMMAND = "getTrainings";
  public static final String DELETE_COMMAND = "delete";
  public static final String GET_NO_ASSIGNED_TRAININGS_COMMAND="getNotAssignedTrainers";

}
