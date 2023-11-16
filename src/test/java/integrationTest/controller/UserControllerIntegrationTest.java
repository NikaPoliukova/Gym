package integrationTest.controller;

import com.epam.upskill.GymApplication;
import com.epam.upskill.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GymApplication.class)
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {
 public static final String USER_SETTING_LOGIN = "/api/v1/users/user/setting/login";
 public static final String USERNAME = "username";
 public static final String OLD_PASSWORD = "oldPassword";
 public static final String NEW_PASSWORD = "newPassword";
 @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserService userService;

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
  void changeLogin_WithValidParams_ThenReturnStatusOk_Test() throws Exception {
    mockMvc.perform(put(USER_SETTING_LOGIN)
            .param(USERNAME, "Ola.Trainee")
            .param(OLD_PASSWORD, "1234567890")
            .param(NEW_PASSWORD, "mM7cahNaEX"))
        .andExpect(status().isOk());
  }

  @Test
  void changeLogin_WithSameOldAndNewPassword_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(put(USER_SETTING_LOGIN)
            .param(USERNAME, "Ola.Trainee")
            .param(OLD_PASSWORD, "mM7cahNaEX")
            .param(NEW_PASSWORD, "mM7cahNaEX"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void changeLogin_WithIncorrectOldPassword_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(put(USER_SETTING_LOGIN)
            .param(USERNAME, "Ola.Trainee")
            .param(OLD_PASSWORD, "7854123658")
            .param(NEW_PASSWORD, "1234567890"))
        .andExpect(status().isNotFound());
  }

  @Test
  void changeLogin_WithMissingParameters_ThenReturnStatusBadRequest_Test() throws Exception {
    mockMvc.perform(put(USER_SETTING_LOGIN))
        .andExpect(status().isBadRequest());
  }

}
