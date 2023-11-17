package integrationTest.controller;

import com.epam.upskill.GymApplication;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
public class RegistrationControllerIntegrationTest {
  private static final String REGISTRATION_TRAINEE_URL = "/api/v1/registration/trainee";
  private static final String REGISTRATION_TRAINER_URL = "/api/v1/registration/trainer";
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String SPECIALIZATION = "specialization";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TraineeService traineeService;
  @Autowired
  private TrainerService trainerService;
  @Autowired
  private UserService userService;


  @Test
  void traineeRegistration_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            post(REGISTRATION_TRAINEE_URL)
                .param(FIRST_NAME, "Trainee")
                .param(LAST_NAME, "Trainee")
                .param("address", "123 Main St")
                .param("dateOfBirth", "1990-01-01")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Trainee.Trainee11"))
        .andExpect(jsonPath("$.password").exists());

  }

  @Test
  void trainerRegistration_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            post(REGISTRATION_TRAINER_URL)
                .param(FIRST_NAME, "Ola")
                .param(LAST_NAME, "Ivanova")
                .param(SPECIALIZATION, "PILATES")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("Ola.Ivanova3"))
        .andExpect(jsonPath("$.password").exists());
  }

  @Test
  void traineeRegistration_WithMissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(post(REGISTRATION_TRAINEE_URL))
        .andExpect(status().isBadRequest());
  }

  @Test
  void trainerRegistration_WithMissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(post(REGISTRATION_TRAINER_URL))
        .andExpect(status().isBadRequest());
  }

}
