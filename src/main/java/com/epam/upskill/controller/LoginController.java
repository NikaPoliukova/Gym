package com.epam.upskill.controller;

import com.epam.upskill.exception.AuthenticationException;
import com.epam.upskill.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Api(tags = "Login")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/login")
public class LoginController {

  private final UserService userService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Authenticate a user")
  public void login(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                    @RequestParam("password") @NotBlank @Size(min = 10, max = 10) String password) {
    var user = userService.findByUsername(username);
    if (!user.getPassword().equals(password)) {
      throw new AuthenticationException(", reason: wrong password");
    }
  }
}
