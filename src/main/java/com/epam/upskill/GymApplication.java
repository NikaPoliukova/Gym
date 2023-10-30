package com.epam.upskill;


import com.epam.upskill.facade.UserFacade;
import com.epam.upskill.util.RandomDataGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class GymApplication implements CommandLineRunner {

//  private static final int NUMBER_OF_OPERATION = 2;
//  private static final String OPERATION_FOR_TRAINER = "OPERATION FOR TRAINER =  ";
//  private static final String OPERATION_FOR_TRAINEE = "OPERATION FOR TRAINEE =  ";
//
//  private static final String USER_DTO_TEMPLATE = """
//      %s%s username =%s password = %s address =%s specialization %s criteria = %s id = %s
//      """;
//
//  private final UserFacade userFacade;
//  private final RandomDataGenerator randomDataGenerator;

  public static void main(String[] args) {
    SpringApplication.run(GymApplication.class, args);
  }

  @Override
  public void run(String... args) {
//    IntStream.range(0, NUMBER_OF_OPERATION).forEach(i -> {
//      var prepareUserDto = randomDataGenerator.prepareTrainer();
//      userFacade.handle(prepareUserDto);
//      log.info(prepareUser(OPERATION_FOR_TRAINER, prepareUserDto));
//    });
//
//    IntStream.range(0, NUMBER_OF_OPERATION).forEach(i -> {
//      var prepareUserDto = randomDataGenerator.prepareTrainee();
//      userFacade.handle(prepareUserDto);
//      log.info(prepareUser(OPERATION_FOR_TRAINEE, prepareUserDto));
//    });
//  }
//
//  private static String prepareUser(String x, PrepareUserDto userDto) {
//    return USER_DTO_TEMPLATE.formatted(x, userDto.operation(), userDto.username(),
//        userDto.password(), userDto.address(), userDto.specialization(),
//        userDto.criteria(), userDto.id());
//  }
  }
}




