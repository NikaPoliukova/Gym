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

@Api(tags = "Login")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
@Validated
public class LoginController {

  private final UserService userService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Authenticate a user")
  public void login(@RequestParam("username") @NotBlank String username,
                    @RequestParam("password") @NotBlank String password) {
    var user = userService.findByUsername(username);
    if (!user.getPassword().equals(password)) {
      throw new AuthenticationException("unauthorized");
    }
  }
}
