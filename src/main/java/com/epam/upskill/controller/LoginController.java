package com.epam.upskill.controller;

import com.epam.upskill.exception.AuthenticationException;
import com.epam.upskill.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Api(tags = "Login")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/login")
public class LoginController {
  @Autowired
  private Counter customRequestsCounter;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private Timer customRequestLatencyTimer;

  private final UserService userService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Authenticate a user")
  public void login(@RequestParam("username") @NotBlank @Size(min = 2, max = 60) String username,
                    @RequestParam("password") @NotBlank @Size(min = 10, max = 10) String password) {
    Timer.Sample sample = Timer.start();
    customRequestsCounter.increment();
    var user = userService.findByUsername(username);
    sample.stop(customRequestLatencyTimer);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new AuthenticationException(", reason: wrong password");
    }
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    request.getSession().invalidate();
  }
}
