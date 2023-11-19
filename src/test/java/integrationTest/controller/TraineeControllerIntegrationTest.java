package integrationTest.controller;

import com.epam.upskill.GymApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class TraineeControllerIntegrationTest {
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String USERNAME = "username";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getTrainee_WithValidUsername_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainees/trainee")
                .param(USERNAME, "Trainee.Trainee")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.firstName").value("Trainee"))
        .andExpect(jsonPath("$.lastName").value("Trainee"))
        .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
        .andExpect(jsonPath("$.address").value("123 Main St"))
        .andExpect(jsonPath("$.isActive").value(true))
        .andExpect(jsonPath("$.trainersList").isArray());
  }

  @Test
  void updateTrainee_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            put("/api/v1/trainees/trainee/setting/profile")
                .param(USERNAME, "Trainee.Trainee")
                .param(FIRST_NAME, "Trainee")
                .param(LAST_NAME, "Trainee")
                .param("isActive", "true")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.firstName").value("Trainee"))
        .andExpect(jsonPath("$.lastName").value("Trainee"))
        .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"))
        .andExpect(jsonPath("$.address").value("123 Main St"))
        .andExpect(jsonPath("$.isActive").value(true))
        .andExpect(jsonPath("$.trainersList").isArray());
  }

  @Test
  void deleteTrainee_WithValidUsername_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            delete("/api/v1/trainees")
                .param(USERNAME, "Trainee.Trainee")
        )
        .andExpect(status().isOk());
  }

  @Test
  void findNotActiveTrainers_WithValidUsername_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainees/not-active-trainers")
                .param(USERNAME, "Trainee.Trainee")
        )
        .andExpect(status().isOk());
  }

  @Test
  void updateTrainerList_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            put("/api/v1/trainees/setting/trainers")
                .param(USERNAME, "Trainee.Trainee")
                .param("trainingDate", "2023-11-17")
                .param("trainingName", "Training1")
                .content("[{\"username\": \"Trainer.Super\"}]")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(content().json("["
            + "{\"username\": \"Trainer.Super\", \"firstName\": \"Trainer\", \"lastName\": \"Super\", " +
            "\"specialization\": \"YOGA\"}]"));
  }

  @Test
  void findTrainingsList_WithInvalidParams_Test() throws Exception {
    mockMvc.perform(
            put("/api/v1/trainees/setting/trainers")
                .param(USERNAME, "Incorrect")
                .param("trainingDate", "2023-11-17")
                .param("trainingName", "Training1")
                .content("[{\"username\": \"Trainer.Super\"}]")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
  }

  @Test
  void toggleActivation_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            patch("/api/v1/trainees/toggle-activation")
                .param(USERNAME, "Trainee.Trainee")
                .param("active", "false")
        )
        .andExpect(status().isOk());
  }
}
