package integrationTest.controller;

import com.epam.upskill.GymApplication;
import com.epam.upskill.service.TraineeService;
import com.epam.upskill.service.TrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@Transactional
public class RegistrationControllerIntegrationTest {
  public static final String REGISTRATION_TRAINEE_URL = "/api/v1/registration/trainee";
  public static final String REGISTRATION_TRAINER_URL = "/api/v1/registration/trainer";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String SPECIALIZATION = "specialization";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private TraineeService traineeService;
  @Autowired
  private TrainerService trainerService;

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void traineeRegistration_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(post(REGISTRATION_TRAINEE_URL)
            .param(FIRST_NAME, "Polina")
            .param(LAST_NAME, "Doe")
            .param("address", "123 Main St")
            .param("dateOfBirth", "1990-01-01"))
        .andExpect(status().isOk());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void trainerRegistration_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(post(REGISTRATION_TRAINER_URL)
            .param(FIRST_NAME, "Jane")
            .param(LAST_NAME, "Smith")
            .param(SPECIALIZATION, "CARDIO"))
        .andExpect(status().isOk());
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
