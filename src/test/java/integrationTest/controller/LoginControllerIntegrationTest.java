package integrationTest.controller;

import com.epam.upskill.GymApplication;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
class LoginControllerIntegrationTest {

  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String URL_TEMPLATE = "/api/v1/login";
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;

  @Test
  void login_WithCorrectParams_ThanReturnStatusOk_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE)
            .param(USERNAME, "Ola.Trainee")
            .param(PASSWORD, "mM7cahNaEX"))
        .andExpect(status().isOk());
  }

  @Test
  void login_WithWrongPassword_ThenReturnStatusNotFound_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE)
            .param(USERNAME, "Ola.Trainee")
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
  void login_MissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(get(URL_TEMPLATE))
        .andExpect(status().isBadRequest());
  }

}

