package com.epam.upskill.controller;

import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.exception.UserUpdateException;
import com.epam.upskill.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
  public void changeLogin(
      @RequestParam("username") @NotBlank String username,
      @RequestParam("oldPassword") @NotBlank String oldPassword,
      @RequestParam("newPassword") @NotBlank @Size(min = 10, max = 10, message = "New password must be 10 characters")
      String newPassword) {
    try {
      if (oldPassword.equals(newPassword)) {
      throw new UserUpdateException("New password cannot be the same as the old password");
      }
      userService.updatePassword(new UserUpdatePass(username, oldPassword, newPassword));
    } catch (UserUpdateException ex) {
      throw new UserUpdateException(username);
    }
  }
}
