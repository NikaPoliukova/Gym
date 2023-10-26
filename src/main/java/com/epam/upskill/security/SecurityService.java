package com.epam.upskill.security;

import com.epam.upskill.entity.User;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityService {
  private final UserService userService;

 /* public User authenticate(Principal principal) {
    var user = userService.findByUsername(principal.username());
    if (user == null) {
      throw new IllegalArgumentException("User not authorized");
    }
    if (!user.getPassword().equals(principal.password())) {
      throw new IllegalArgumentException("Invalid password");
    }
    return user;
  }*/
}
