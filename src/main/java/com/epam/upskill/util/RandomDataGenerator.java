package com.epam.upskill.util;

import com.epam.upskill.dto.PrepareUserDto;
import com.epam.upskill.security.Principal;
import lombok.experimental.UtilityClass;

import java.security.SecureRandom;

@UtilityClass
public class RandomDataGenerator {
  private final SecureRandom random = new SecureRandom();

  ///for Trainer
  private static final String[] usernames = {"Pi.Vova1"};
  private static final String[] passwords = {"n02GYZWQaK"};
  private static final long[] ids = {34L};
  ///for Trainee
  private static final String[] usernames2 = {"Nika.Nika1"};
  private static final String[] passwords2 = {"nhMlJHUOiF"};
  private final long[] ids2 = {36L};

  private static final String[] operations = {"get", "updatePassword", "update", "toggleActivation", "getTrainings"};
  private static final String[] criteriaArray = {"Training 1", "Training 2", "Training 3"};
  private static final String[] specializations = {"SPEC", "Yoga"};

  public static PrepareUserDto prepareTrainer() {

    long id = ids[random.nextInt(ids.length)];
    var principal = new Principal(usernames[0], passwords[0]);
    return buildUserDto(id, principal);
  }

  public static PrepareUserDto prepareTrainee() {
    long id = ids2[random.nextInt(ids.length)];
    var principal = new Principal(usernames2[0], passwords2[0]);
    return buildUserDto(id, principal);
  }

  private static String generateSpecialization() {
    int randomIndex = random.nextInt(specializations.length);
    return specializations[randomIndex];
  }

  public String generateRandomOperation() {
    int randomIndex = random.nextInt(operations.length);
    return operations[randomIndex];
  }

  public String generateRandomCriteria() {
    int randomIndex = random.nextInt(criteriaArray.length);
    return criteriaArray[randomIndex];
  }

  private static PrepareUserDto buildUserDto(long id, Principal principal) {
    String address = "address" + random.nextInt(1000);
    String specialization = generateSpecialization();
    String criteria = generateRandomCriteria();
    String operation = generateRandomOperation();
    return new PrepareUserDto(id, principal.username(), principal.password(), address, specialization, criteria,
        operation);
  }
}
