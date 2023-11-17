package integrationTest.controller;

import com.epam.upskill.GymApplication;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
class LoginControllerIntegrationTest {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String URL_TEMPLATE = "/api/v1/login";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;

  @Test
  void login_WithCorrectParams_ThanReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            get(URL_TEMPLATE)
                .param(USERNAME, "Trainee.Trainee")
                .param(PASSWORD, "VrGvccsnZW")
        )
        .andExpect(status().isOk());
  }

  @Test
  void login_WithWrongPassword_ThenReturnStatusNotFound_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE)
            .param(USERNAME, "Trainee.Trainee")
            .param(PASSWORD, "1234567891"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void login_NonExistingUser_ThenReturnStatusNotFound_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE)
            .param(USERNAME, "nonExistingUser")
            .param(PASSWORD, "1234567891"))
        .andExpect(status().isNotFound());
  }

  @Test
  void login_WithWrongUsername_ThenReturnStatusUnauthorized_Test() throws Exception {
    mockMvc.perform(get("/api/v1/login")
            .param(USERNAME, "NonExistingUser")
            .param(PASSWORD, "validPassword"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void login_MissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE))
        .andExpect(status().isBadRequest());
  }

}

