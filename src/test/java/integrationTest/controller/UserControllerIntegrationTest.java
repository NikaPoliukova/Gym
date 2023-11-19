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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.config.activate.on-profile=test")
@EnableTransactionManagement
@ActiveProfiles("test")
@Transactional
class UserControllerIntegrationTest {
  private static final String USER_SETTING_LOGIN = "/api/v1/users/user/setting/login";
  private final String USERNAME = "username";
  private final String OLD_PASSWORD = "oldPassword";
  private final String NEW_PASSWORD = "newPassword";

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;

  @Test
  void changeLogin_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(
            put(USER_SETTING_LOGIN)
                .param(USERNAME, "Trainee.Trainee")
                .param(OLD_PASSWORD, "VrGvccsnZW")
                .param(NEW_PASSWORD, "1234567890")
        )
        .andExpect(status().isOk());
  }

  @Test
  void changeLogin_WithSameOldAndNewPassword_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(put
            (USER_SETTING_LOGIN)
            .param(USERNAME, "Trainee.Trainee")
            .param(OLD_PASSWORD, "VrGvccsnZW")
            .param(NEW_PASSWORD, "VrGvccsnZW")
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void changeLogin_WithIncorrectOldPassword_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(put
            (USER_SETTING_LOGIN)
            .param(USERNAME, "Trainee.Trainee")
            .param(OLD_PASSWORD, "7854123658")
            .param(NEW_PASSWORD, "1234567890")
        )
        .andExpect(status().isNotFound());
  }

  @Test
  void changeLogin_WithMissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(
            put(USER_SETTING_LOGIN)
        )
        .andExpect(status().isBadRequest());
  }
}
