package com.epam.upskill.controller;

import com.epam.upskill.dto.UserUpdatePass;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

//Работает
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserController {
  private final UserService userService;

  @PutMapping("/change-login")
  public ResponseEntity<String> changeLogin( @RequestParam String username,
                                             @RequestParam String oldPassword,
                                             @RequestParam @Size(min = 10, max = 10,
                                                 message = "New password must be 10 characters") String newPassword) {
    if (oldPassword.equals(newPassword)) {
      return ResponseEntity.badRequest().body("New password cannot be the same as the old password");
    }
    var userUpdatePass = new UserUpdatePass(username, oldPassword, newPassword);
    userService.updatePassword(userUpdatePass);
    return ResponseEntity.ok("200 OK");
  }
}
