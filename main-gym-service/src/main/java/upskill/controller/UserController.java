package upskill.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import upskill.dto.UserUpdatePass;
import upskill.exception.OperationFailedException;
import upskill.service.UserService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/users/user")
public class UserController {
  private final UserService userService;

  @PutMapping("/setting/login")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Change login")
  public void changeLogin(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                          @RequestParam("oldPassword") @NotBlank @Size(min = 10, max = 10) String oldPassword,
                          @RequestParam("newPassword") @NotBlank @Size(min = 10, max = 10, message = "New password must" +
                              " be 10 characters") String newPassword) {
    if (oldPassword.equals(newPassword)) {
      throw new OperationFailedException(username + ": password cannot be the same as the old password",
          "change password");
    }
    userService.updatePassword(new UserUpdatePass(username, oldPassword, newPassword));
  }
}
