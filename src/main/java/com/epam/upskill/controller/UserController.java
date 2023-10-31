package com.epam.upskill.controller;

import com.epam.upskill.dto.UserDto;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {
  private final UserService userService;

  @PutMapping("/change-login")
  public ResponseEntity<String> changeLogin(@RequestBody UserDto request) {
    userService.updateLogin(request);
    return ResponseEntity.ok("200 OK");
  }
}
