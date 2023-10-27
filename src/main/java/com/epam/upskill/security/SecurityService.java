package com.epam.upskill.security;

import com.epam.upskill.entity.User;
import com.epam.upskill.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SecurityService {
  private final UserService userService;

 public boolean authenticate(Principal principal) {
    var user = userService.findByUsername(principal.username());
    if (user.isEmpty()) {
      throw new IllegalArgumentException("User not authorized");
    }
    if (!user.get().getPassword().equals(principal.password())) {
      return false;
    }
    return true;
  }
}
