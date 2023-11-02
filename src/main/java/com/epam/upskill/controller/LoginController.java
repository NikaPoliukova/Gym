package com.epam.upskill.controller;

import com.epam.upskill.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//РАБОТАЕТ
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

  private final UserService userService;

  @GetMapping()
  @ApiOperation("Authenticate a user")
  public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
    var user = userService.findByUsername(username);
    if (user.getPassword().equals(password)) {
      return ResponseEntity.ok("200 OK");
    } else {
      return ResponseEntity.status(401).body("Unauthorized");
    }
  }
}
