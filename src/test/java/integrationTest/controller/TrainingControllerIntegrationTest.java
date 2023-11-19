package integrationTest.controller;

import com.epam.upskill.GymApplication;
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

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class TrainingControllerIntegrationTest {

  private static final String TRAINING_NAME = "trainingName";
  private static final String TRAINING_DURATION = "trainingDuration";
  private static final String TRAINING_TYPE = "trainingType";
  private static final String DATE = "date";
  private static final String TRAINEE_USERNAME = "traineeUsername";
  private static final String TRAINER_USERNAME = "trainerUsername";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void saveTraining_ReturnsOk_WhenTrainingSavedSuccessfully() throws Exception {
    mockMvc.perform(
            post("/api/v1/trainings/new-training")
                .param(TRAINEE_USERNAME, "Trainee.Trainee")
                .param(TRAINER_USERNAME, "Trainer.Trainer2")
                .param(TRAINING_NAME, "Training3")
                .param(DATE, LocalDate.now().toString())
                .param(TRAINING_TYPE, "YOGA")
                .param(TRAINING_DURATION, "50")
        )
        .andExpect(status().isOk());
  }

  @Test
  void saveTraining_ReturnsBadRequest_WhenParametersAreInvalid() throws Exception {
    mockMvc.perform(
            post("/api/v1/trainings/new-training")
                .param(TRAINEE_USERNAME, "Trainee.Incorrect")
                .param(TRAINER_USERNAME, "Trainer.Trainer")
                .param(TRAINING_NAME, "Training1")
                .param(DATE, LocalDate.now().toString())
                .param(TRAINING_TYPE, "CARDIO")
                .param(TRAINING_DURATION, "60")
        )
        .andExpect(status().isNotFound());
  }

  @Test
  void getTrainingTypes_ReturnsListOfTrainingTypes_WhenCalled() throws Exception {
    mockMvc.perform(
            get("/api/v1/trainings/training-types"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json")
        )
        .andExpect(jsonPath("$", hasSize(5)));
  }
}
