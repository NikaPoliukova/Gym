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
class TrainerControllerIntegrationTest {
  private static final String FIRST_NAME = "firstName";
  private static final String LAST_NAME = "lastName";
  private static final String USERNAME = "username";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void getTrainer_ReturnsTrainerResponse_WhenUsernameExists() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainers/trainer")
                .param(USERNAME, "Trainer.Trainer")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.firstName").value("Trainer"))
        .andExpect(jsonPath("$.lastName").value("Trainer"));
  }

  @Test
  void getTrainer_ReturnsNotFound_WhenUsernameDoesNotExist() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainers/trainer")
                .param(USERNAME, "nonExistingTrainer")
        )
        .andExpect(status().isNotFound());

  }

  @Test
  void updateTrainer_ReturnsTrainerUpdateResponse_WhenUpdatedSuccessfully() throws Exception {
    mockMvc.perform(
            put("/api/v1/trainers/trainer/setting/profile")
                .param(USERNAME, "Trainer.Trainer")
                .param(FIRST_NAME, "TrainerNew")
                .param(LAST_NAME, "Trainer")
                .param("specialization", "CARDIO")
                .param("isActive", "false")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.firstName").value("TrainerNew"))
        .andExpect(jsonPath("$.lastName").value("Trainer"))
        .andExpect(jsonPath("$.specialization").value("CARDIO"))
        .andExpect(jsonPath("$.isActive").value(false));
  }

  @Test
  void updateTrainer_ReturnsBadRequest_WhenUsernameIsInvalid() throws Exception {
    // Выполнение запроса с некорректным именем пользователя
    mockMvc.perform(put("/api/v1/trainers/trainer/setting/profile")
            .param("username", "   ") // некорректное имя пользователя
            .param("firstName", "TrainerNew")
            .param("lastName", "Trainer")
            .param("specialization", "CARDIO")
            .param("isActive", "false"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void findTrainerTrainingsList_WithValidParams_Test() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainers/trainer/trainings")
                .param(USERNAME, "Trainer.Trainer")
                .param("traineeName", "Trainee.Trainee")
        )
        .andExpect(status().isOk());
  }

  @Test
  void findTrainingsList_WithInvalidParams_Test() throws Exception {
    mockMvc.perform(
            put("/api/v1/trainees/setting/trainers")
                .param(USERNAME, "Trainer.Incorrect")
                .param("trainingDate", "2023-11-17")
                .param("trainingName", "Training1")
                .content("[{\"username\": \"Trainee.Trainee\"}]")
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isNotFound());
  }

  @Test
  void toggleActivation_TogglesActivation_WhenCalled() throws Exception {

    mockMvc.perform(patch("/api/v1/trainers/trainer/toggle-activation")
            .param(USERNAME, "Trainer.Trainer")
            .param("active", "false"))
        .andExpect(status().isOk());
  }
}
